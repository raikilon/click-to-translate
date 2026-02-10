package ch.clicktotranslate.translation.infrastructure;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;

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
