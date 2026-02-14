package ch.clicktotranslate.tokenizer;

import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;
import ch.clicktotranslate.tokenizer.domain.SegmentBundleTokenizedEvent;
import ch.clicktotranslate.translation.infrastructure.TextToTranslateDto;
import ch.clicktotranslate.translation.infrastructure.TextTranslationFacade;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ApplicationModuleTest
class SegmentBundleTokenizationModuleTest {

	@MockitoBean
	private TextTranslationFacade textTranslationFacade;

	@Test
	void givenTranslatedSegmentBundleEvent_whenTokenize_thenPublishesTokenizedEventAndCallsTranslationFacadeOnce(
			Scenario scenario) {
		TestContext context = new TestContext();
		context.givenTranslation();

		scenario.publish(context.segmentBundleCreatedEvent)
			.andWaitForEventOfType(SegmentBundleTokenizedEvent.class)
			.matching(context::eventMatches)
			.toArriveAndVerify(event -> assertThat(event).isEqualTo(context.expectedEvent));

		context.verifyTranslationCall();
	}

	private final class TestContext {

		private final Instant occurredAt = Instant.parse("2026-02-07T10:15:30Z");

		private final String userId = "user-1";

		private final String word = "Haus Katze";

		private final String sentence = "Das Haus ist gross.";

		private final String wordTranslation = "House cat";

		private final String sentenceTranslation = "The house is big.";

		private final String sourceLanguage = "DE";

		private final String targetLanguage = "EN";

		private final String tokenizedWord = "Haus";

		private final String tokenizedWordTranslation = "House";

		private final SegmentBundleCreatedEvent segmentBundleCreatedEvent = new SegmentBundleCreatedEvent(userId, word,
				sentence, wordTranslation, sentenceTranslation, sourceLanguage, targetLanguage,
				new SegmentBundleCreatedEvent.Source("page", "42", "Readme"),
				new SegmentBundleCreatedEvent.GenericSourceMetadata("https://example.com", "example.com", 3, 1),
				occurredAt);

		private final TextToTranslateDto translationRequest = new TextToTranslateDto(tokenizedWord,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.DE,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.EN);

		private final SegmentBundleTokenizedEvent expectedEvent = new SegmentBundleTokenizedEvent(userId, tokenizedWord,
				tokenizedWordTranslation, sentence, sentenceTranslation, word, wordTranslation, sourceLanguage,
				targetLanguage, occurredAt);

		private void givenTranslation() {
			given(textTranslationFacade.translate(translationRequest)).willReturn(tokenizedWordTranslation);
		}

		private void verifyTranslationCall() {
			verify(textTranslationFacade, times(1)).translate(translationRequest);
		}

		private boolean eventMatches(SegmentBundleTokenizedEvent event) {
			return event.equals(expectedEvent);
		}

	}

}
