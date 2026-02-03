package ch.clicktotranslate.translation.framework.spring.http.outbound;

import ch.clicktotranslate.translation.domain.entity.TranslationRequest;
import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;

import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.client.DeepLApiClient;

public class SpringDeepLApiClient implements DeepLApiClient {

    private final DeepLClient client;

    public SpringDeepLApiClient(String authKey) {
        if (authKey == null || authKey.isBlank()) {
            throw new IllegalArgumentException("DeepL auth key is required.");
        }
        this.client = new DeepLClient(authKey);
    }

    @Override
    public String translate(TranslationRequest request) {
        String sourceLanguage = request.sourceLanguage();
        String targetLanguage = request.targetLanguage();
        String translatedText = null;

        try {
            if (request.text() != null && !request.text().isBlank()) {
                TextResult textResult = client.translateText(request.text(), sourceLanguage, targetLanguage);
                translatedText = textResult.getText();
            }
        } catch (DeepLException | InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("DeepL translation failed.", exception);
        }

        return translatedText;
    }
}
