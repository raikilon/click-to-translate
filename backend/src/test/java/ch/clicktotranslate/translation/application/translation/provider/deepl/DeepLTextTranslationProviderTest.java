package ch.clicktotranslate.translation.application.translation.provider.deepl;

import ch.clicktotranslate.translation.domain.Language;
import ch.clicktotranslate.translation.domain.TextToTranslate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class DeepLTextTranslationProviderTest {

	@Test
	void givenDeeplProvider_whenTranslate_thenCallsClientWithMappedLanguages() {
		TestContext context = new TestContext();
		context.givenTranslationSucceeds();

		String result = context.underTest.translate(context.request);

		assertThat(result).isEqualTo(context.expectedTranslation);
		context.verifyTranslationCalled();
		verifyNoMoreInteractions(context.deepLTextTranslation);
	}

	private final class TestContext {

		private final DeepLTextTranslation deepLTextTranslation = mock(DeepLTextTranslation.class);

		private final DeepLLanguageMapper languageMapper = new DeepLLanguageMapper();

		private final DeepLTextTranslationProvider underTest = new DeepLTextTranslationProvider(deepLTextTranslation,
				languageMapper);

		private final String word = "Haus";

		private final Language sourceLanguage = Language.DE;

		private final Language targetLanguage = Language.EN;

		private final String sourceLanguageCode = "de";

		private final String targetLanguageCode = "en";

		private final TextToTranslate request = new TextToTranslate(word, sourceLanguage, targetLanguage);

		private final String expectedTranslation = "House";

		private void givenTranslationSucceeds() {
			given(deepLTextTranslation.translate(word, sourceLanguageCode, targetLanguageCode))
				.willReturn(expectedTranslation);
		}

		private void verifyTranslationCalled() {
			verify(deepLTextTranslation).translate(word, sourceLanguageCode, targetLanguageCode);
		}

	}

}
