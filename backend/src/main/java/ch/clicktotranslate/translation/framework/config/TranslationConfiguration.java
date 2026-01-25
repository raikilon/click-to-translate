package ch.clicktotranslate.translation.framework.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import ch.clicktotranslate.translation.domain.outbound.EventPublisher;
import ch.clicktotranslate.translation.domain.outbound.TranslationGateway;
import ch.clicktotranslate.translation.domain.usecase.TranslateWord;
import ch.clicktotranslate.translation.framework.spring.events.outbound.SpringEventPublisherAdapter;
import ch.clicktotranslate.translation.framework.spring.http.inbound.mapper.HttpTranslateRequestMapper;
import ch.clicktotranslate.translation.framework.spring.http.inbound.mapper.HttpTranslateResponseMapper;
import ch.clicktotranslate.translation.framework.spring.http.outbound.SpringDeepLApiClient;
import ch.clicktotranslate.translation.framework.spring.http.outbound.mapper.DeepLTranslateRequestMapper;
import ch.clicktotranslate.translation.framework.spring.http.outbound.mapper.DeepLTranslateResponseMapper;
import ch.clicktotranslate.translation.infrastructure.controller.TranslateWordController;
import ch.clicktotranslate.translation.infrastructure.gateway.DeepLTranslationGateway;
import ch.clicktotranslate.translation.infrastructure.gateway.client.DeepLApiClient;

@Configuration
public class TranslationConfiguration {
	@Bean
	public TranslateWord translateWord(TranslationGateway translationGateway, EventPublisher eventPublisher) {
		return new TranslateWord(translationGateway, eventPublisher);
	}

	@Bean
	public TranslateWordController translateWordController(TranslateWord translateWord) {
		return new TranslateWordController(translateWord);
	}

	@Bean
	public HttpTranslateRequestMapper httpTranslateRequestMapper() {
		return new HttpTranslateRequestMapper();
	}

	@Bean
	public HttpTranslateResponseMapper httpTranslateResponseMapper() {
		return new HttpTranslateResponseMapper();
	}

	@Bean
	public DeepLTranslateRequestMapper deepLTranslateRequestMapper() {
		return new DeepLTranslateRequestMapper();
	}

	@Bean
	public DeepLTranslateResponseMapper deepLTranslateResponseMapper() {
		return new DeepLTranslateResponseMapper();
	}

	@Bean
	public TranslationGateway translationGateway(DeepLApiClient deepLApiClient) {
		return new DeepLTranslationGateway(deepLApiClient);
	}

	@Bean
	public DeepLApiClient deepLApiClient(RestTemplate restTemplate, DeepLTranslateRequestMapper requestMapper,
			DeepLTranslateResponseMapper responseMapper) {
		return new SpringDeepLApiClient(restTemplate, requestMapper, responseMapper);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		return new SpringEventPublisherAdapter(applicationEventPublisher);
	}
}
