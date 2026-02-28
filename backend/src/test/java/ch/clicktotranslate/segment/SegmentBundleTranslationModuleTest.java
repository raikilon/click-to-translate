package ch.clicktotranslate.segment;

import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;
import ch.clicktotranslate.segment.infrastructure.LanguageDto;
import ch.clicktotranslate.segment.infrastructure.SegmentBundleDto;
import ch.clicktotranslate.segment.infrastructure.SegmentBundleTranslationRestController;
import ch.clicktotranslate.segment.infrastructure.TranslatedSegmentDto;
import ch.clicktotranslate.auth.UserProvider;
import ch.clicktotranslate.auth.UserId;
import ch.clicktotranslate.translation.infrastructure.TextToTranslateDto;
import ch.clicktotranslate.translation.infrastructure.TextTranslationFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ApplicationModuleTest
class SegmentBundleTranslationModuleTest {

	@Autowired
	private SegmentBundleTranslationRestController underTest;

	@MockitoBean
	private TextTranslationFacade textTranslationFacade;

	@MockitoBean
	private UserProvider userProvider;

	@MockitoBean
	private JwtDecoder jwtDecoder;

	@Test
	void givenSegmentBundle_whenTranslate_thenReturnsTranslationPublishesEventAndCallsTranslationModuleTwice(
			Scenario scenario) {
		TestContext context = new TestContext();
		context.givenTextTranslations();
		context.givenUser();

		scenario.stimulate(() -> underTest.translate(context.segmentBundle))
			.andWaitForEventOfType(SegmentBundleCreatedEvent.class)
			.matching(context::eventMatches)
			.toArriveAndVerify((event, response) -> {
				assertThat(response).isEqualTo(context.expectedResponse);
				assertThat(event).isEqualTo(context.expectedEvent);
			});

		context.verifyTranslationCalls();

	}

	private final class TestContext {

		private final Instant occurredAt = Instant.parse("2026-02-07T10:15:30Z");

		private final String word = "Haus";

		private final String sentence = "Das Haus ist gross.";

		private final String translatedWord = "House";

		private final String translatedSentence = "The house is big.";

		private final String userId = "user-1";

		private final LanguageDto sourceLanguage = LanguageDto.DE;

		private final LanguageDto targetLanguage = LanguageDto.EN;

		private final SegmentBundleDto segmentBundle = new SegmentBundleDto(word, sentence, sourceLanguage,
				targetLanguage, occurredAt);

		private final TextToTranslateDto wordTranslationRequest = new TextToTranslateDto(word,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.DE,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.EN, sentence);

		private final TextToTranslateDto sentenceTranslationRequest = new TextToTranslateDto(sentence,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.DE,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.EN, null);

		private final TranslatedSegmentDto expectedResponse = new TranslatedSegmentDto(translatedWord);

		private final SegmentBundleCreatedEvent expectedEvent = new SegmentBundleCreatedEvent(userId, word, sentence,
				translatedWord, translatedSentence, sourceLanguage.name(), targetLanguage.name(), occurredAt);

		private void givenTextTranslations() {
			given(textTranslationFacade.translate(wordTranslationRequest)).willReturn(translatedWord);
			given(textTranslationFacade.translate(sentenceTranslationRequest)).willReturn(translatedSentence);
		}

		private void givenUser() {
			given(userProvider.currentUserId()).willReturn(UserId.of(userId));
		}

		private void verifyTranslationCalls() {
			verify(textTranslationFacade, times(1)).translate(wordTranslationRequest);
			verify(textTranslationFacade, times(1)).translate(sentenceTranslationRequest);
		}

		private boolean eventMatches(SegmentBundleCreatedEvent event) {
			return event.equals(expectedEvent);
		}

	}

}
