package ch.clicktotranslate.translation.framework.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import ch.clicktotranslate.translation.domain.outbound.EventPublisher;
import ch.clicktotranslate.translation.domain.outbound.TranslationService;
import ch.clicktotranslate.translation.domain.usecase.TranslateWordUseCase;
import ch.clicktotranslate.translation.framework.spring.events.outbound.SpringEventPublisher;
import ch.clicktotranslate.translation.framework.spring.events.outbound.mapper.TranslatedWordEventMapper;
import ch.clicktotranslate.translation.framework.spring.http.inbound.mapper.HttpTranslateRequestMapper;
import ch.clicktotranslate.translation.framework.spring.http.inbound.mapper.HttpTranslateResponseMapper;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.mapper.DeepLTranslateRequestMapper;
import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.mapper.DeepLTranslateResponseMapper;

@Configuration
public class TranslationConfiguration {

    @Bean
    public TranslateWordUseCase translateWord(TranslationService translationService, EventPublisher eventPublisher) {
        return new TranslateWordUseCase(translationService, eventPublisher);
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
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TranslatedWordEventMapper translatedWordEventMapper() {
        return new TranslatedWordEventMapper();
    }

    @Bean
    public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher,
            TranslatedWordEventMapper eventMapper) {
        return new SpringEventPublisher(applicationEventPublisher, eventMapper);
    }
}
