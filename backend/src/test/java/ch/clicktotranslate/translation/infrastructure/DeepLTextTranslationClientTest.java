package ch.clicktotranslate.translation.infrastructure;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class DeepLTextTranslationClientTest {

	@Test
	void givenTranslationSucceeds_whenTranslate_thenReturnsTranslatedText() throws Exception {
		TestContext context = new TestContext();
		context.givenTranslationSucceeds();

		String result = context.underTest.translate(context.text, context.sourceLanguage, context.targetLanguage);

		assertThat(result).isEqualTo(context.translatedText);
		context.verifyTranslationCalled();
		context.verifyTranslationResultRead();
		verifyNoMoreInteractions(context.client, context.textResult);
	}

	@Test
	void givenTranslationThrowsDeepLException_whenTranslate_thenThrowsIllegalStateExceptionAndInterruptsThread()
			throws Exception {
		TestContext context = new TestContext();
		context.givenTranslationThrowsDeepLException();

		assertThatThrownBy(
				() -> context.underTest.translate(context.text, context.sourceLanguage, context.targetLanguage))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("DeepL translation failed.")
			.hasCauseInstanceOf(DeepLException.class);

		assertThat(Thread.currentThread().isInterrupted()).isTrue();
		context.verifyTranslationCalled();
		verifyNoMoreInteractions(context.client);
	}

	private final class TestContext {

		private final DeepLClient client = mock(DeepLClient.class);

		private final DeepLTextTranslationClient underTest = new DeepLTextTranslationClient(client);

		private final String text = "Hallo";

		private final String sourceLanguage = "de";

		private final String targetLanguage = "en";

		private final String translatedText = "Hello";

		private TextResult textResult;

		private void givenTranslationSucceeds() throws DeepLException, InterruptedException {
			textResult = mock(TextResult.class);
			given(textResult.getText()).willReturn(translatedText);
			given(client.translateText(text, sourceLanguage, targetLanguage)).willReturn(textResult);
		}

		private void givenTranslationThrowsDeepLException() throws DeepLException, InterruptedException {
			given(client.translateText(text, sourceLanguage, targetLanguage))
				.willThrow(new DeepLException("translation failed"));
		}

		private void verifyTranslationCalled() throws DeepLException, InterruptedException {
			verify(client).translateText(text, sourceLanguage, targetLanguage);
		}

		private void verifyTranslationResultRead() {
			verify(textResult).getText();
		}

	}

}
