package ch.clicktotranslate.translation.framework.intermodule.inbound;

import org.springframework.stereotype.Service;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.domain.entity.TranslatedWord;
import ch.clicktotranslate.translation.framework.intermodule.inbound.mapper.TranslateRequestMapper;
import ch.clicktotranslate.translation.framework.intermodule.inbound.mapper.TranslateResponseMapper;
import ch.clicktotranslate.translation.infrastructure.controller.TranslateWordController;

@Service
public class TranslationFacade {

    private final TranslateWordController translateWordController;
    private final TranslateRequestMapper requestMapper;
    private final TranslateResponseMapper responseMapper;

    public TranslationFacade(
            TranslateWordController translateWordController,
            TranslateRequestMapper requestMapper,
            TranslateResponseMapper responseMapper) {
        this.translateWordController = translateWordController;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    public TranslateResponseDto translate(TranslateRequestDto request) {
        TranslateWord input = requestMapper.map(request);
        TranslatedWord result = translateWordController.translate(input);
        return responseMapper.map(result);
    }
}
