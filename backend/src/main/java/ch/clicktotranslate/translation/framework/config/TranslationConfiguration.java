package ch.clicktotranslate.translation.framework.config;

import java.util.List;

import ch.clicktotranslate.translation.infrastructure.controller.mapper.TranslateWordMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import ch.clicktotranslate.translation.infrastructure.event.EventPublisher;
import ch.clicktotranslate.translation.infrastructure.event.TranslatedWordEventMapper;
import ch.clicktotranslate.translation.domain.outbound.TranslationService;
import ch.clicktotranslate.translation.domain.usecase.TranslateWordUseCase;
import ch.clicktotranslate.translation.framework.spring.events.outbound.SpringEventPublisher;
import ch.clicktotranslate.translation.framework.spring.http.inbound.mapper.HttpTranslateRequestMapper;
import ch.clicktotranslate.translation.framework.spring.http.inbound.mapper.HttpTranslateResponseMapper;
import ch.clicktotranslate.translation.framework.intermodule.inbound.mapper.TranslateRequestMapper;
import ch.clicktotranslate.translation.framework.intermodule.inbound.mapper.TranslateResponseMapper;
import ch.clicktotranslate.translation.framework.spring.http.outbound.SpringDeepLApiClient;
import ch.clicktotranslate.translation.infrastructure.controller.TranslationController;
import ch.clicktotranslate.translation.infrastructure.service.ProviderRoutingTranslationService;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationProvider;
import ch.clicktotranslate.translation.infrastructure.service.strategy.TranslationStrategy;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.DeepLTranslationStrategy;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.client.DeepLApiClient;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.mapper.DeepLTranslateResponseMapper;

@Configuration
public class TranslationConfiguration {

    @Bean
    public TranslateWordUseCase translateWord(TranslationService translationService) {
        return new TranslateWordUseCase(translationService);
    }

    @Bean
    public TranslationController translateWordController(TranslateWordUseCase translateWordUseCase,
            EventPublisher eventPublisher, TranslatedWordEventMapper eventFactory, TranslateWordMapper translateWordMapper) {
        return new TranslationController(translateWordUseCase, eventPublisher, eventFactory, translateWordMapper);
    }

    @Bean
    public TranslateWordMapper translateWordMapper() {
        return new TranslateWordMapper();
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
    public TranslateRequestMapper translateRequestMapper() {
        return new TranslateRequestMapper();
    }

    @Bean
    public TranslateResponseMapper translateResponseMapper() {
        return new TranslateResponseMapper();
    }

    @Bean
    public DeepLTranslateResponseMapper deepLTranslateResponseMapper() {
        return new DeepLTranslateResponseMapper();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DeepLApiClient deepLApiClient(@Value("${deepl.auth-key:}") String authKey) {
        return new SpringDeepLApiClient(authKey);
    }

    @Bean
    public TranslationStrategy deepLTranslationStrategy(DeepLApiClient apiClient, DeepLTranslateResponseMapper responseMapper) {
        return new DeepLTranslationStrategy(apiClient, responseMapper);
    }

    @Bean
    public TranslationProvider defaultTranslationProvider() {
        return TranslationProvider.DEEPL;
    }

    @Bean
    public TranslationService translationDomainService(List<TranslationStrategy> strategyList,
            TranslationProvider defaultProvider) {
        return new ProviderRoutingTranslationService(strategyList, defaultProvider);
    }

    @Bean
    public ch.clicktotranslate.translation.framework.spring.events.outbound.mapper.TranslatedWordEventMapper translatedWordEventMapper() {
        return new ch.clicktotranslate.translation.framework.spring.events.outbound.mapper.TranslatedWordEventMapper();
    }

    @Bean
    public TranslatedWordEventMapper translatedWordEventFactory() {
        return new TranslatedWordEventMapper();
    }

    @Bean
    public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher,
            ch.clicktotranslate.translation.framework.spring.events.outbound.mapper.TranslatedWordEventMapper eventMapper) {
        return new SpringEventPublisher(applicationEventPublisher, eventMapper);
    }
}
