package ch.clicktotranslate.vocabulary.framework.spring.http.outbound.mapper;

import ch.clicktotranslate.translation.framework.intermodule.inbound.TranslateRequestDto;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationRequest;

/**
 * Maps infrastructure-level translation request to translation module's
 * intermodule DTO. Framework layer mapper that handles the cross-module
 * communication.
 */
public class TranslationRequestToTranslateRequestMapper {

    public TranslateRequestDto map(TranslationRequest request) {
        return new TranslateRequestDto();
    }
}
