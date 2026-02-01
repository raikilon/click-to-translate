package ch.clicktotranslate.translation.framework.spring.http.outbound.mapper;

import ch.clicktotranslate.translation.framework.spring.http.outbound.dto.DeepLHttpTranslateResponse;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.dto.DeepLTranslateResponse;

/**
 * Maps HTTP-specific DeepL response to infrastructure-level response.
 */
public class DeepLHttpResponseMapper {

    public DeepLTranslateResponse map(DeepLHttpTranslateResponse httpResponse) {
        // TODO: Map HTTP DTO to infrastructure DTO
        return new DeepLTranslateResponse();
    }
}
