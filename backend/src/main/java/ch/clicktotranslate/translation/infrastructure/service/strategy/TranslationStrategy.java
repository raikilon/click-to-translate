package ch.clicktotranslate.translation.infrastructure.service.strategy;

import ch.clicktotranslate.translation.domain.entity.TranslationRequest;

public interface TranslationStrategy {

    String translate(TranslationRequest request);

    TranslationProvider getProvider();
}
