package ch.clicktotranslate.translation.infrastructure.service.strategy.deepl;

import ch.clicktotranslate.translation.domain.entity.TranslationRequest;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationProvider;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationStrategy;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.client.DeepLApiClient;

public class DeepLTranslationStrategy implements TranslationStrategy {


    private final DeepLApiClient apiClient;

    public DeepLTranslationStrategy(DeepLApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public String translate(TranslationRequest request) {
        return apiClient.translate(request);
    }

    @Override
    public TranslationProvider getProvider() {
        return TranslationProvider.DEEPL;
    }
}
