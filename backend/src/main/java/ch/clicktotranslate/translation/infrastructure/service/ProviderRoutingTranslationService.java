package ch.clicktotranslate.translation.infrastructure.service;

import java.util.List;

import java.util.EnumMap;
import java.util.Map;

import ch.clicktotranslate.translation.domain.entity.TranslationRequest;
import ch.clicktotranslate.translation.domain.outbound.TranslationService;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationProvider;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationStrategy;

public class ProviderRoutingTranslationService implements TranslationService {
    private final Map<TranslationProvider, TranslationStrategy> strategyByProvider;
    private final TranslationProvider defaultProvider;

    public ProviderRoutingTranslationService(List<TranslationStrategy> strategyList, TranslationProvider defaultProvider) {
        this.defaultProvider = defaultProvider;
        this.strategyByProvider = new EnumMap<>(TranslationProvider.class);
        for (TranslationStrategy strategy : strategyList) {
            this.strategyByProvider.put(strategy.getProvider(), strategy);
        }
    }

    @Override
    public String translate(TranslationRequest request) {
        TranslationStrategy strategy = strategyByProvider.get(defaultProvider);
        if (strategy == null) {
            throw new IllegalStateException("No translation strategy configured for provider: " + defaultProvider);
        }
        return strategy.translate(request);
    }

}
