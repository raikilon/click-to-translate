package ch.clicktotranslate.translation.framework.spring.http.outbound;

import ch.clicktotranslate.translation.domain.entity.TranslationRequest;
import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;

import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.client.DeepLApiClient;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.dto.DeepLTranslateResponse;

public class SpringDeepLApiClient implements DeepLApiClient {

    private final DeepLClient client;

    public SpringDeepLApiClient(String authKey) {
        if (authKey == null || authKey.isBlank()) {
            throw new IllegalArgumentException("DeepL auth key is required.");
        }
        this.client = new DeepLClient(authKey);
    }

    @Override
    public DeepLTranslateResponse translate(TranslationRequest request) {
        DeepLTranslateResponse response = new DeepLTranslateResponse();
        String sourceLanguage = request.getSourceLanguage();
        String targetLanguage = request.getTargetLanguage();

        try {
            if (request.getText() != null && !request.getText().isBlank()) {
                TextResult textResult = client.translateText(request.getText(), sourceLanguage, targetLanguage);
                response.setTranslatedText(textResult.getText());
            }
        } catch (DeepLException | InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("DeepL translation failed.", exception);
        }

        return response;
    }
}
