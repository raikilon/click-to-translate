package ch.clicktotranslate.vocabulary.infrastructure.gateway;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.TranslatedLemma;
import ch.clicktotranslate.vocabulary.domain.outbound.TranslationGateway;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationRequest;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationResponse;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper.LemmaToTranslationRequestMapper;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper.TranslationResponseToTranslatedLemmaMapper;

public class ClickToTranslateTranslationService implements TranslationGateway {

	private final ClickToTranslateTranslationServiceApiClient apiClient;

	private final LemmaToTranslationRequestMapper requestMapper;

	private final TranslationResponseToTranslatedLemmaMapper responseMapper;

	public ClickToTranslateTranslationService(ClickToTranslateTranslationServiceApiClient apiClient,
			LemmaToTranslationRequestMapper requestMapper, TranslationResponseToTranslatedLemmaMapper responseMapper) {
		this.apiClient = apiClient;
		this.requestMapper = requestMapper;
		this.responseMapper = responseMapper;
	}

	@Override
	public TranslatedLemma translate(Lemma lemma) {
		TranslationRequest request = requestMapper.map(lemma);
		TranslationResponse response = apiClient.translate(request);
		return responseMapper.map(response);
	}

}
