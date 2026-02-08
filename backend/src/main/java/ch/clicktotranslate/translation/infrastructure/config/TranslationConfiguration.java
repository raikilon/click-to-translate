package ch.clicktotranslate.translation.infrastructure.config;

import java.util.List;

import ch.clicktotranslate.translation.infrastructure.event.TranslatedSegmentBundleEventMapper;
import ch.clicktotranslate.translation.application.web.SegmentBundleMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import ch.clicktotranslate.translation.application.event.EventPublisher;
import ch.clicktotranslate.translation.application.event.TranslatedWordEventMapper;
import ch.clicktotranslate.translation.domain.TextTranslation;
import ch.clicktotranslate.translation.domain.SegmentTranslation;
import ch.clicktotranslate.translation.infrastructure.event.SpringEventPublisher;
import ch.clicktotranslate.translation.infrastructure.web.SegmentDtoMapper;
import ch.clicktotranslate.translation.infrastructure.web.DeepLTextTranslationClient;
import ch.clicktotranslate.translation.infrastructure.web.LanguageDtoMapper;
import ch.clicktotranslate.translation.application.web.SegmentBundleTranslationController;
import ch.clicktotranslate.translation.application.web.TranslationLanguageController;
import ch.clicktotranslate.translation.application.DefaultTextTranslation;
import ch.clicktotranslate.translation.application.translation.provider.TextTranslationProviderType;
import ch.clicktotranslate.translation.application.translation.TextTranslationProvider;
import ch.clicktotranslate.translation.application.translation.provider.deepl.DeepLTextTranslationProvider;
import ch.clicktotranslate.translation.application.translation.provider.deepl.DeepLTextTranslation;
import ch.clicktotranslate.translation.application.translation.provider.deepl.DeepLLanguageMapper;

@Configuration
public class TranslationConfiguration {

	@Bean
	public SegmentTranslation translateWord(TextTranslation textTranslation) {
		return new SegmentTranslation(textTranslation);
	}

	@Bean
	public SegmentBundleTranslationController translateWordController(SegmentTranslation segmentTranslation,
			EventPublisher eventPublisher, TranslatedWordEventMapper eventFactory,
			SegmentBundleMapper segmentBundleMapper) {
		return new SegmentBundleTranslationController(segmentTranslation, eventPublisher, eventFactory,
				segmentBundleMapper);
	}

	@Bean
	public SegmentBundleMapper translateWordMapper() {
		return new SegmentBundleMapper();
	}

	@Bean
	public TranslationLanguageController translationLanguageController() {
		return new TranslationLanguageController();
	}

	@Bean
	public SegmentDtoMapper httpTranslateResponseMapper(LanguageDtoMapper languageDtoMapper) {
		return new SegmentDtoMapper(languageDtoMapper);
	}

	@Bean
	public LanguageDtoMapper languageDtoMapper() {
		return new LanguageDtoMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public DeepLLanguageMapper deepLLanguageMapper() {
		return new DeepLLanguageMapper();
	}

	@Bean
	public DeepLTextTranslation deepLApiClient(@Value("${deepl.auth-key}") String authKey) {
		return new DeepLTextTranslationClient(authKey);
	}

	@Bean
	public TextTranslationProvider deepLTranslationStrategy(DeepLTextTranslation apiClient,
			DeepLLanguageMapper languageMapper) {
		return new DeepLTextTranslationProvider(apiClient, languageMapper);
	}

	@Bean
	public TextTranslationProviderType defaultTranslationProvider() {
		return TextTranslationProviderType.DEEPL;
	}

	@Bean
	public TextTranslation translationDomainService(List<TextTranslationProvider> strategyList,
			TextTranslationProviderType defaultProvider) {
		return new DefaultTextTranslation(strategyList, defaultProvider);
	}

	@Bean
	public TranslatedSegmentBundleEventMapper translatedWordEventMapper() {
		return new TranslatedSegmentBundleEventMapper();
	}

	@Bean
	public TranslatedWordEventMapper translatedWordEventFactory() {
		return new TranslatedWordEventMapper();
	}

	@Bean
	public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher,
			TranslatedSegmentBundleEventMapper eventMapper) {
		return new SpringEventPublisher(applicationEventPublisher, eventMapper);
	}

}
