package ch.clicktotranslate.translation.application;

import ch.clicktotranslate.translation.application.translation.TextTranslationProvider;
import ch.clicktotranslate.translation.application.translation.provider.TextTranslationProviderType;
import ch.clicktotranslate.translation.domain.Language;
import ch.clicktotranslate.translation.domain.TextToTranslate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class DefaultTextTranslationTest {

	@Test
	void givenDeeplProviderAvailable_whenTranslate_thenDelegatesToDeepl() {
		TestContext context = new TestContext();
		context.givenProviders(TextTranslationProviderType.DEEPL,
				List.of(context.deeplProvider, context.googleProvider));
		context.givenDeeplTranslationSucceeds();

		String result = context.underTest.translate(context.request);

		assertThat(result).isEqualTo(context.expectedTranslation);
		context.verifyProvidersRegisteredWithDefaultList();
		context.verifyDeeplTranslation();
		verifyNoMoreInteractions(context.deeplProvider, context.googleProvider);
	}

	@Test
	void givenMissingProvider_whenTranslate_thenThrowsIllegalStateException() {
		TestContext context = new TestContext();
		context.givenProviders(context.missingProvider, List.of(context.deeplProvider));

		assertThatThrownBy(() -> context.underTest.translate(context.request)).isInstanceOf(IllegalStateException.class)
			.hasMessageContaining(context.missingProviderMessage);

		context.verifyDeeplProviderRegistered();
		verifyNoInteractions(context.googleProvider);
		verifyNoMoreInteractions(context.deeplProvider);
	}

	private final class TestContext {

		private final TextTranslationProvider deeplProvider = mock(TextTranslationProvider.class);

		private final TextTranslationProvider googleProvider = mock(TextTranslationProvider.class);

		private final String word = "Haus";

		private final Language sourceLanguage = Language.DE;

		private final Language targetLanguage = Language.EN;

		private final TextToTranslate request = new TextToTranslate(word, sourceLanguage, targetLanguage);

		private final String expectedTranslation = "House";

		private final TextTranslationProviderType missingProvider = TextTranslationProviderType.GOOGLE;

		private final String missingProviderMessage = "No translation textTranslation configured for provider: "
				+ missingProvider;

		private DefaultTextTranslation underTest;

		private void givenProviders(TextTranslationProviderType defaultProvider,
				List<TextTranslationProvider> providers) {
			given(deeplProvider.getProvider()).willReturn(TextTranslationProviderType.DEEPL);
			given(googleProvider.getProvider()).willReturn(TextTranslationProviderType.GOOGLE);
			underTest = new DefaultTextTranslation(providers, defaultProvider);
		}

		private void givenDeeplTranslationSucceeds() {
			given(deeplProvider.translate(request)).willReturn(expectedTranslation);
		}

		private void verifyProvidersRegisteredWithDefaultList() {
			verify(deeplProvider).getProvider();
			verify(googleProvider).getProvider();
		}

		private void verifyDeeplProviderRegistered() {
			verify(deeplProvider).getProvider();
		}

		private void verifyDeeplTranslation() {
			verify(deeplProvider).translate(request);
		}

	}

}
