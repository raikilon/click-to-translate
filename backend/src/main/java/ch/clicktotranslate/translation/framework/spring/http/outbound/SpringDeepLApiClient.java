package ch.clicktotranslate.translation.framework.spring.http.outbound;

import org.springframework.web.client.RestTemplate;

import ch.clicktotranslate.translation.framework.spring.http.outbound.mapper.DeepLHttpRequestMapper;
import ch.clicktotranslate.translation.framework.spring.http.outbound.mapper.DeepLHttpResponseMapper;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.client.DeepLApiClient;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.dto.DeepLTranslateRequest;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.dto.DeepLTranslateResponse;

public class SpringDeepLApiClient implements DeepLApiClient {

    private static final String DEEPL_TRANSLATE_URI = "/deepl/translate";

    private final RestTemplate restTemplate;
    private final DeepLHttpRequestMapper requestMapper;
    private final DeepLHttpResponseMapper responseMapper;

    public SpringDeepLApiClient(RestTemplate restTemplate, DeepLHttpRequestMapper requestMapper,
            DeepLHttpResponseMapper responseMapper) {
        this.restTemplate = restTemplate;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @Override
    public DeepLTranslateResponse translate(DeepLTranslateRequest request) {
        return null;
    }
}
