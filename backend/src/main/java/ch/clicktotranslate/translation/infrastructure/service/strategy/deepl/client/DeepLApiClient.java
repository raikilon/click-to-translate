package ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.client;

import ch.clicktotranslate.translation.domain.entity.TranslationRequest;

public interface DeepLApiClient {

    String translate(TranslationRequest request);
}
