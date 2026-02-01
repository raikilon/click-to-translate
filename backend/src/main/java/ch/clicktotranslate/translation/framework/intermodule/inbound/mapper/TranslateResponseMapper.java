package ch.clicktotranslate.translation.framework.intermodule.inbound.mapper;

import ch.clicktotranslate.translation.domain.entity.TranslatedWord;
import ch.clicktotranslate.translation.framework.intermodule.inbound.TranslateResponseDto;

/**
 * Maps domain entity to inter-module response DTO.
 */
public class TranslateResponseMapper {

    public TranslateResponseDto map(TranslatedWord translatedWord) {
        return new TranslateResponseDto();
    }
}
