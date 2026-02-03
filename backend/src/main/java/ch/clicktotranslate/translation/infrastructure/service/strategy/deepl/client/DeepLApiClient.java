package ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.client;

import ch.clicktotranslate.translation.domain.entity.TranslationRequest;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.dto.DeepLTranslateResponse;

public interface DeepLApiClient {

    DeepLTranslateResponse translate(TranslationRequest request);
}
