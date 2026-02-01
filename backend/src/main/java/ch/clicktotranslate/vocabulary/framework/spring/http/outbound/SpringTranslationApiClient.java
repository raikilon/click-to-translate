package ch.clicktotranslate.vocabulary.framework.spring.http.outbound;

import ch.clicktotranslate.translation.framework.intermodule.inbound.TranslateRequestDto;
import ch.clicktotranslate.translation.framework.intermodule.inbound.TranslateResponseDto;
import ch.clicktotranslate.translation.framework.intermodule.inbound.TranslationFacade;
import ch.clicktotranslate.vocabulary.framework.spring.http.outbound.mapper.TranslateResponseToTranslationResponseMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.outbound.mapper.TranslationRequestToTranslateRequestMapper;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.ClickToTranslateTranslationServiceApiClient;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationRequest;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationResponse;

public class SpringTranslationApiClient implements ClickToTranslateTranslationServiceApiClient {

    private final TranslationFacade translationFacade;
    private final TranslationRequestToTranslateRequestMapper requestMapper;
    private final TranslateResponseToTranslationResponseMapper responseMapper;

    public SpringTranslationApiClient(
            TranslationFacade translationFacade,
            TranslationRequestToTranslateRequestMapper requestMapper,
            TranslateResponseToTranslationResponseMapper responseMapper) {
        this.translationFacade = translationFacade;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @Override
    public TranslationResponse translate(TranslationRequest request) {
        TranslateRequestDto translateRequest = requestMapper.map(request);

        TranslateResponseDto translateResponse = translationFacade.translate(translateRequest);

        return responseMapper.map(translateResponse);
    }
}
