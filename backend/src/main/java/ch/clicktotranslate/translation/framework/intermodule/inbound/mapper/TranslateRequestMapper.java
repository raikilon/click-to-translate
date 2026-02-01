package ch.clicktotranslate.translation.framework.intermodule.inbound.mapper;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.framework.intermodule.inbound.TranslateRequestDto;

/**
 * Maps inter-module request DTO to domain entity.
 */
public class TranslateRequestMapper {

    public TranslateWord map(TranslateRequestDto request) {
        return new TranslateWord();
    }
}
