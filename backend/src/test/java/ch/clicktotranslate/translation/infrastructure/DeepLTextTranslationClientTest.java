package ch.clicktotranslate.translation.infrastructure;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.TextTranslationOptions;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class DeepLTextTranslationClientTest {

	@Test
	void givenTranslationSucceeds_whenTranslate_thenReturnsTranslatedText() throws Exception {
		TestContext context = new TestContext();
		context.givenTranslationSucceeds();

		String result = context.underTest.translate(context.text, context.sourceLanguage, context.targetLanguage, null);

		assertThat(result).isEqualTo(context.translatedText);
		context.verifyTranslationCalled();
		context.verifyTranslationResultRead();
		verifyNoMoreInteractions(context.client, context.textResult);
	}

	@Test
	void givenContextAndTranslationSucceeds_whenTranslate_thenCallsClientWithContextOptions() throws Exception {
		TestContext context = new TestContext();
		context.givenTranslationWithContextSucceeds();

		String result = context.underTest.translate(context.text, context.sourceLanguage, context.targetLanguage,
				context.translationContext);

		assertThat(result).isEqualTo(context.translatedText);
		context.verifyTranslationCalledWithContext();
		context.verifyTranslationResultRead();
		verifyNoMoreInteractions(context.client, context.textResult);
	}

	@Test
	void givenTranslationThrowsDeepLException_whenTranslate_thenThrowsIllegalStateExceptionWithoutInterruptingThread()
			throws Exception {
		TestContext context = new TestContext();
		context.givenTranslationThrowsDeepLException();

		assertThatThrownBy(
				() -> context.underTest.translate(context.text, context.sourceLanguage, context.targetLanguage, null))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("DeepL translation failed.")
			.hasCauseInstanceOf(DeepLException.class);

		assertThat(Thread.currentThread().isInterrupted()).isFalse();
		context.verifyTranslationCalled();
		verifyNoMoreInteractions(context.client);
	}

	@Test
	void givenTranslationThrowsInterruptedException_whenTranslate_thenThrowsIllegalStateExceptionAndInterruptsThread()
			throws Exception {
		TestContext context = new TestContext();
		context.givenTranslationThrowsInterruptedException();

		assertThatThrownBy(
				() -> context.underTest.translate(context.text, context.sourceLanguage, context.targetLanguage, null))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("DeepL translation failed.")
			.hasCauseInstanceOf(InterruptedException.class);

		assertThat(Thread.currentThread().isInterrupted()).isTrue();
		Thread.interrupted();
		context.verifyTranslationCalled();
		verifyNoMoreInteractions(context.client);
	}

	private final class TestContext {

		private final DeepLClient client = mock(DeepLClient.class);

		private final DeepLTextTranslationClient underTest = new DeepLTextTranslationClient(client);

		private final String text = "Hallo";

		private final String sourceLanguage = "de";

		private final String targetLanguage = "en";

		private final String translationContext = "Das Haus ist alt.";

		private final String translatedText = "Hello";

		private TextResult textResult;

		private void givenTranslationSucceeds() throws DeepLException, InterruptedException {
			textResult = mock(TextResult.class);
			given(textResult.getText()).willReturn(translatedText);
			given(client.translateText(text, sourceLanguage, targetLanguage)).willReturn(textResult);
		}

		private void givenTranslationWithContextSucceeds() throws DeepLException, InterruptedException {
			textResult = mock(TextResult.class);
			given(textResult.getText()).willReturn(translatedText);
			given(client.translateText(eq(text), eq(sourceLanguage), eq(targetLanguage),
					any(TextTranslationOptions.class)))
				.willReturn(textResult);
		}

		private void givenTranslationThrowsDeepLException() throws DeepLException, InterruptedException {
			given(client.translateText(text, sourceLanguage, targetLanguage))
				.willThrow(new DeepLException("translation failed"));
		}

		private void givenTranslationThrowsInterruptedException() throws DeepLException, InterruptedException {
			given(client.translateText(text, sourceLanguage, targetLanguage))
				.willThrow(new InterruptedException("translation interrupted"));
		}

		private void verifyTranslationCalled() throws DeepLException, InterruptedException {
			verify(client).translateText(text, sourceLanguage, targetLanguage);
		}

		private void verifyTranslationCalledWithContext() throws DeepLException, InterruptedException {
			ArgumentCaptor<TextTranslationOptions> optionsCaptor = ArgumentCaptor
				.forClass(TextTranslationOptions.class);
			verify(client).translateText(eq(text), eq(sourceLanguage), eq(targetLanguage), optionsCaptor.capture());
			assertThat(optionsCaptor.getValue().getContext()).isEqualTo(translationContext);
		}

		private void verifyTranslationResultRead() {
			verify(textResult).getText();
		}

	}

}
