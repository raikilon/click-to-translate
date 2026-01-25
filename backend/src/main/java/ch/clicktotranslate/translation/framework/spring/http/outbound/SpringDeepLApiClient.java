package ch.clicktotranslate.translation.framework.spring.http.outbound;

import org.springframework.web.client.RestTemplate;

import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordInput;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordOutput;
import ch.clicktotranslate.translation.framework.spring.http.outbound.mapper.DeepLTranslateRequestMapper;
import ch.clicktotranslate.translation.framework.spring.http.outbound.mapper.DeepLTranslateResponseMapper;
import ch.clicktotranslate.translation.infrastructure.gateway.client.DeepLApiClient;

public class SpringDeepLApiClient implements DeepLApiClient {
	private static final String DEEPL_TRANSLATE_URI = "/deepl/translate";

	private final RestTemplate restTemplate;
	private final DeepLTranslateRequestMapper requestMapper;
	private final DeepLTranslateResponseMapper responseMapper;

	public SpringDeepLApiClient(RestTemplate restTemplate, DeepLTranslateRequestMapper requestMapper,
			DeepLTranslateResponseMapper responseMapper) {
		this.restTemplate = restTemplate;
		this.requestMapper = requestMapper;
		this.responseMapper = responseMapper;
	}

	@Override
	public TranslateWordOutput translate(TranslateWordInput request) {
		return null;
	}
}
