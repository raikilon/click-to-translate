package ch.clicktotranslate.translation.infrastructure.service;

import java.util.List;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.domain.entity.TranslatedWord;
import ch.clicktotranslate.translation.domain.outbound.TranslationService;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationProvider;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationStrategy;

public class ProviderRoutingTranslationService implements TranslationService {

    public ProviderRoutingTranslationService(List<TranslationStrategy> strategyList, TranslationProvider defaultProvider) {

    }

    @Override
    public TranslatedWord translate(TranslateWord input) {
        return null;
    }

}
