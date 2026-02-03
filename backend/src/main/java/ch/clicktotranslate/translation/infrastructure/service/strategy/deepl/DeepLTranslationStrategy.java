package ch.clicktotranslate.translation.infrastructure.service.strategy.deepl;

import ch.clicktotranslate.translation.domain.entity.TranslationRequest;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationProvider;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationStrategy;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.client.DeepLApiClient;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.mapper.DeepLTranslateResponseMapper;

public class DeepLTranslationStrategy implements TranslationStrategy {

    private final DeepLTranslateResponseMapper responseMapper;

    private final DeepLApiClient apiClient;

    public DeepLTranslationStrategy(DeepLApiClient apiClient,
            DeepLTranslateResponseMapper responseMapper) {
        this.apiClient = apiClient;
        this.responseMapper = responseMapper;
    }

    @Override
    public String translate(TranslationRequest request) {
        return this.responseMapper.map(apiClient.translate(request));
    }

    @Override
    public TranslationProvider getProvider() {
        return TranslationProvider.DEEPL;
    }
}
