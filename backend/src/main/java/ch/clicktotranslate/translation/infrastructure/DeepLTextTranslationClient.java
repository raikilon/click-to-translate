package ch.clicktotranslate.translation.infrastructure;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.TextTranslationOptions;

import ch.clicktotranslate.translation.application.DeepLTextTranslation;

public class DeepLTextTranslationClient implements DeepLTextTranslation {

	private final DeepLClient client;

	public DeepLTextTranslationClient(DeepLClient client) {
		if (client == null) {
			throw new IllegalArgumentException("DeepL client is required.");
		}
		this.client = client;
	}

	@Override
	public String translate(String text, String sourceLanguage, String targetLanguage, String context) {
		String translatedText = null;

		try {
			if (text != null && !text.isBlank()) {
				TextResult textResult = translateText(text, sourceLanguage, targetLanguage, context);
				translatedText = textResult.getText();
			}
		}
		catch (DeepLException | InterruptedException exception) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("DeepL translation failed.", exception);
		}

		return translatedText;
	}

	private TextResult translateText(String text, String sourceLanguage, String targetLanguage, String context)
			throws DeepLException, InterruptedException {
		if (context == null || context.isBlank()) {
			return client.translateText(text, sourceLanguage, targetLanguage);
		}

		TextTranslationOptions options = new TextTranslationOptions().setContext(context);
		return client.translateText(text, sourceLanguage, targetLanguage, options);
	}

}
