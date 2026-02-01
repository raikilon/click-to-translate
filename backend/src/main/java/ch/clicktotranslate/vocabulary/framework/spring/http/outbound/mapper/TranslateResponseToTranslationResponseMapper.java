package ch.clicktotranslate.vocabulary.framework.spring.http.outbound.mapper;

import ch.clicktotranslate.translation.framework.intermodule.inbound.TranslateResponseDto;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationResponse;

/**
 * Maps translation module's intermodule DTO to infrastructure-level translation
 * response. Framework layer mapper that handles the cross-module communication.
 */
public class TranslateResponseToTranslationResponseMapper {

    public TranslationResponse map(TranslateResponseDto response) {
        return new TranslationResponse();
    }
}
