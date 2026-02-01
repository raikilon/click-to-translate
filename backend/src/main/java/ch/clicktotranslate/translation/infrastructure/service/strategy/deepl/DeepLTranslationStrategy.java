package ch.clicktotranslate.translation.infrastructure.service.strategy.deepl;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.domain.entity.TranslatedWord;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationProvider;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationStrategy;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.client.DeepLApiClient;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.mapper.DeepLTranslateRequestMapper;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.mapper.DeepLTranslateResponseMapper;

public class DeepLTranslationStrategy implements TranslationStrategy {

    private final DeepLTranslateRequestMapper requestMapper;
    private final DeepLTranslateResponseMapper responseMapper;

    private final DeepLApiClient apiClient;

    public DeepLTranslationStrategy(DeepLApiClient apiClient, DeepLTranslateRequestMapper requestMapper,
            DeepLTranslateResponseMapper responseMapper) {
        this.apiClient = apiClient;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @Override
    public TranslatedWord translate(TranslateWord input) {
        return this.responseMapper.map(apiClient.translate(this.requestMapper.map(input)));
    }

    @Override
    public TranslationProvider getProvider() {
        return TranslationProvider.DEEPL;
    }
}
