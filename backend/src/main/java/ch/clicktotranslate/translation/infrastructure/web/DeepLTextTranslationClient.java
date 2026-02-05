package ch.clicktotranslate.translation.infrastructure.web;

import ch.clicktotranslate.translation.domain.TextToTranslate;
import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;

import ch.clicktotranslate.translation.application.translation.provider.deepl.DeepLTextTranslation;
import org.jmolecules.architecture.onion.simplified.DomainRing;
import org.jmolecules.architecture.onion.simplified.InfrastructureRing;

public class DeepLTextTranslationClient implements DeepLTextTranslation {

	private final DeepLClient client;

	public DeepLTextTranslationClient(String authKey) {
		if (authKey == null || authKey.isBlank()) {
			throw new IllegalArgumentException("DeepL auth key is required.");
		}
		this.client = new DeepLClient(authKey);
	}

	@Override
	public String translate(TextToTranslate request) {
		String sourceLanguage = request.sourceLanguage();
		String targetLanguage = request.targetLanguage();
		String translatedText = null;

		try {
			if (request.text() != null && !request.text().isBlank()) {
				TextResult textResult = client.translateText(request.text(), sourceLanguage, targetLanguage);
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
