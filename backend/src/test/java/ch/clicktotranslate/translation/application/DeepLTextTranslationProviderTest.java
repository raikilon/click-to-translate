package ch.clicktotranslate.translation.application;

import ch.clicktotranslate.translation.domain.TextToTranslate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class DeepLTextTranslationProviderTest {

	@Test
	void givenTargetLanguageIsEn_whenTranslate_thenCallsClientWithEnGbTarget() {
		TestContext context = new TestContext();
		context.givenTranslationSucceedsForGermanToEnglish();

		String result = context.underTest.translate(context.germanToEnglishRequest);

		assertThat(result).isEqualTo(context.expectedTranslation);
		context.verifyGermanToEnglishTranslationCalled();
		verifyNoMoreInteractions(context.deepLTextTranslation);
	}

	@Test
	void givenSourceLanguageIsEn_whenTranslate_thenCallsClientWithEnGbSource() {
		TestContext context = new TestContext();
		context.givenTranslationSucceedsForEnglishToGerman();

		String result = context.underTest.translate(context.englishToGermanRequest);

		assertThat(result).isEqualTo(context.expectedTranslation);
		context.verifyEnglishToGermanTranslationCalled();
		verifyNoMoreInteractions(context.deepLTextTranslation);
	}

	private final class TestContext {

		private final DeepLTextTranslation deepLTextTranslation = mock(DeepLTextTranslation.class);

		private final DeepLLanguageMapper languageMapper = new DeepLLanguageMapper();

		private final DeepLTextTranslationProvider underTest = new DeepLTextTranslationProvider(deepLTextTranslation,
				languageMapper);

		private final String word = "Haus";

		private final String sourceLanguage = "DE";

		private final String targetLanguage = "EN";

		private final String englishLanguage = "EN";

		private final String germanLanguage = "DE";

		private final String sourceLanguageCode = "de";

		private final String targetLanguageCode = "en-GB";

		private final String englishLanguageCode = "en-GB";

		private final String germanLanguageCode = "de";

		private final String context = "Das Haus ist alt.";

		private final TextToTranslate germanToEnglishRequest = new TextToTranslate(word, sourceLanguage, targetLanguage,
				context);

		private final TextToTranslate englishToGermanRequest = new TextToTranslate(word, englishLanguage,
				germanLanguage, context);

		private final String expectedTranslation = "House";

		private void givenTranslationSucceedsForGermanToEnglish() {
			given(deepLTextTranslation.translate(word, sourceLanguageCode, targetLanguageCode, context))
				.willReturn(expectedTranslation);
		}

		private void givenTranslationSucceedsForEnglishToGerman() {
			given(deepLTextTranslation.translate(word, englishLanguageCode, germanLanguageCode, context))
				.willReturn(expectedTranslation);
		}

		private void verifyGermanToEnglishTranslationCalled() {
			verify(deepLTextTranslation).translate(word, sourceLanguageCode, targetLanguageCode, context);
		}

		private void verifyEnglishToGermanTranslationCalled() {
			verify(deepLTextTranslation).translate(word, englishLanguageCode, germanLanguageCode, context);
		}

	}

}
