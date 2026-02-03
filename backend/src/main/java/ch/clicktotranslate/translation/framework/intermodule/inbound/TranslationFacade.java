package ch.clicktotranslate.translation.framework.intermodule.inbound;

import org.springframework.stereotype.Service;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.domain.entity.TranslatedWord;
import ch.clicktotranslate.translation.domain.usecase.TranslateWordUseCase;
import ch.clicktotranslate.translation.framework.intermodule.inbound.mapper.TranslateRequestMapper;
import ch.clicktotranslate.translation.framework.intermodule.inbound.mapper.TranslateResponseMapper;

@Service
public class TranslationFacade {

    private final TranslateWordUseCase translateWordUseCase;
    private final TranslateRequestMapper requestMapper;
    private final TranslateResponseMapper responseMapper;

    public TranslationFacade(
            TranslateWordUseCase translateWordUseCase,
            TranslateRequestMapper requestMapper,
            TranslateResponseMapper responseMapper) {
        this.translateWordUseCase = translateWordUseCase;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    public TranslateResponseDto translate(TranslateRequestDto request) {
        TranslateWord input = requestMapper.map(request);
        TranslatedWord result = translateWordUseCase.execute(input);
        return responseMapper.map(result);
    }
}
