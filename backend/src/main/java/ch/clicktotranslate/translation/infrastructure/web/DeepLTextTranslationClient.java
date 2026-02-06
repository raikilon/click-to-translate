package ch.clicktotranslate.translation.infrastructure.web;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;

import ch.clicktotranslate.translation.application.translation.provider.deepl.DeepLTextTranslation;

public class DeepLTextTranslationClient implements DeepLTextTranslation {

	private final DeepLClient client;

	public DeepLTextTranslationClient(String authKey) {
		if (authKey == null || authKey.isBlank()) {
			throw new IllegalArgumentException("DeepL auth key is required.");
		}
		this.client = new DeepLClient(authKey);
	}

	@Override
	public String translate(String text, String sourceLanguage, String targetLanguage) {
		String translatedText = null;

		try {
			if (text != null && !text.isBlank()) {
				TextResult textResult = client.translateText(text, sourceLanguage, targetLanguage);
				translatedText = textResult.getText();
			}
		}
		catch (DeepLException | InterruptedException exception) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("DeepL translation failed.", exception);
		}

		return translatedText;
	}

}
