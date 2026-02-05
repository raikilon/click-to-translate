package ch.clicktotranslate.vocabulary.framework.spring.http.outbound;

import ch.clicktotranslate.vocabulary.infrastructure.gateway.ClickToTranslateTranslationServiceApiClient;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationRequest;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationResponse;

public class SpringTranslationApiClient implements ClickToTranslateTranslationServiceApiClient {

	// private final TextToTranslateController textToTranslateController;
	// private final TranslationRequestToTranslateRequestMapper requestMapper;
	// private final TranslateResponseToTranslationResponseMapper responseMapper;
	//
	// public SpringTranslationApiClient(
	// TextToTranslateController textToTranslateController,
	// TranslationRequestToTranslateRequestMapper requestMapper,
	// TranslateResponseToTranslationResponseMapper responseMapper) {
	// this.textToTranslateController = textToTranslateController;
	// this.requestMapper = requestMapper;
	// this.responseMapper = responseMapper;
	// }
	//
	@Override
	public TranslationResponse translate(TranslationRequest request) {
		return null;
	}

}
