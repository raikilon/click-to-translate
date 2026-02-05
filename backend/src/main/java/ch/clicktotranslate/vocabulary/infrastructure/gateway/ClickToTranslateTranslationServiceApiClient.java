package ch.clicktotranslate.vocabulary.infrastructure.gateway;

import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationRequest;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationResponse;

public interface ClickToTranslateTranslationServiceApiClient {

	TranslationResponse translate(TranslationRequest request);

}
