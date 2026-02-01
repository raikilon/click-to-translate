package ch.clicktotranslate.translation;

import ch.clicktotranslate.translation.application.translation.provider.deepl.DeepLTextTranslation;
import ch.clicktotranslate.translation.infrastructure.event.TranslatedSegmentBundleEventDto;
import ch.clicktotranslate.translation.infrastructure.web.LanguageDto;
import ch.clicktotranslate.translation.infrastructure.web.SegmentBundleDto;
import ch.clicktotranslate.translation.infrastructure.web.SegmentBundleTranslationRestController;
import ch.clicktotranslate.translation.infrastructure.web.TranslatedSegmentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ApplicationModuleTest
class SegmentBundleTranslationModuleTest {

	@Autowired
	private SegmentBundleTranslationRestController underTest;

	@MockitoBean
	private DeepLTextTranslation deepLTextTranslation;

	@Test
	void givenSegmentBundle_whenTranslate_thenReturnsTranslationPublishesEventAndCallsDeepLClientTwice(
			Scenario scenario) {
		TestContext context = new TestContext();
		context.givenDeepLTranslations();

		scenario.stimulate(() -> underTest.translate(context.segmentBundle))
			.andWaitForEventOfType(TranslatedSegmentBundleEventDto.class)
			.matching(context::eventMatches)
			.toArriveAndVerify((event, response) -> {
				assertThat(response).isEqualTo(context.expectedResponse);
				assertThat(event).isEqualTo(context.expectedEvent);
			});

		context.verifyDeepLCalls();
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

		private final String sourceLanguageCode = "DE";

		private final String targetLanguageCode = "EN";

		private final String sourceLanguageTranslationCode = "de";

		private final String targetLanguageTranslationCode = "en";

		private final String sourceType = "page";

		private final String sourceId = "42";

		private final String sourceTitle = "Readme";

		private final String sourceUrl = "https://example.com";

		private final String sourceDomain = "example.com";

		private final Integer selectionOffset = 3;

		private final Integer paragraphIndex = 1;

		private final SegmentBundleDto.SourceDto source = new SegmentBundleDto.SourceDto(sourceType, sourceId,
				sourceTitle);

		private final SegmentBundleDto.SourceMetadataDto sourceMetadata = new SegmentBundleDto.GenericSourceMetadataDto(
				sourceUrl, sourceDomain, selectionOffset, paragraphIndex);

		private final SegmentBundleDto segmentBundle = new SegmentBundleDto(userId, word, sentence, sourceLanguage,
				targetLanguage, source, sourceMetadata, occurredAt);

		private final TranslatedSegmentDto expectedResponse = new TranslatedSegmentDto(word, sentence, translatedWord,
				translatedSentence);

		private final TranslatedSegmentBundleEventDto expectedEvent = new TranslatedSegmentBundleEventDto(userId, word,
				sentence, translatedWord, translatedSentence, sourceLanguageCode, targetLanguageCode,
				new TranslatedSegmentBundleEventDto.SourceDto(sourceType, sourceId, sourceTitle),
				new TranslatedSegmentBundleEventDto.GenericSourceMetadataDto(sourceUrl, sourceDomain, selectionOffset,
						paragraphIndex),
				occurredAt);

		private void givenDeepLTranslations() {
			given(deepLTextTranslation.translate(word, sourceLanguageTranslationCode, targetLanguageTranslationCode))
				.willReturn(translatedWord);
			given(deepLTextTranslation.translate(sentence, sourceLanguageTranslationCode,
					targetLanguageTranslationCode))
				.willReturn(translatedSentence);
		}

		private boolean eventMatches(TranslatedSegmentBundleEventDto event) {
			return expectedEvent.equals(event);
		}

		private void verifyDeepLCalls() {
			verify(deepLTextTranslation, times(1)).translate(word, sourceLanguageTranslationCode,
					targetLanguageTranslationCode);
			verify(deepLTextTranslation, times(1)).translate(sentence, sourceLanguageTranslationCode,
					targetLanguageTranslationCode);
		}

	}

}
