package ch.clicktotranslate.translation.framework.spring.http.outbound.mapper;

import ch.clicktotranslate.translation.framework.spring.http.outbound.dto.DeepLHttpTranslateRequest;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.dto.DeepLTranslateRequest;

/**
 * Maps infrastructure-level DeepL request to HTTP-specific request format.
 */
public class DeepLHttpRequestMapper {

    public DeepLHttpTranslateRequest map(DeepLTranslateRequest request) {
        // TODO: Map infrastructure DTO to HTTP DTO
        return new DeepLHttpTranslateRequest();
    }
}
