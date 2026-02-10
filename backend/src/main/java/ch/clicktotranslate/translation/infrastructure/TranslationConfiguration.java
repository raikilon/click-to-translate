package ch.clicktotranslate.translation.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import ch.clicktotranslate.translation.application.TextTranslationProvider;
import ch.clicktotranslate.translation.application.TextTranslationProviderType;
import ch.clicktotranslate.translation.application.DeepLLanguageMapper;
import ch.clicktotranslate.translation.application.DeepLTextTranslation;
import ch.clicktotranslate.translation.application.DeepLTextTranslationProvider;
import ch.clicktotranslate.translation.application.TextTranslationController;
import ch.clicktotranslate.translation.application.TranslationLanguageController;
import ch.clicktotranslate.translation.infrastructure.DeepLTextTranslationClient;
import ch.clicktotranslate.translation.infrastructure.LanguageDtoMapper;

@Configuration
public class TranslationConfiguration {

	@Bean
	public TranslationLanguageController translationLanguageController() {
		return new TranslationLanguageController();
	}

	@Bean
	public LanguageDtoMapper languageDtoMapper() {
		return new LanguageDtoMapper();
	}

	@Bean
	public TextToTranslateDtoMapper textToTranslateDtoMapper() {
		return new TextToTranslateDtoMapper();
	}

	@Bean
	public TextTranslationController textTranslationController(List<TextTranslationProvider> translationProviders) {
		return new TextTranslationController(translationProviders, TextTranslationProviderType.DEEPL);
	}

	@Bean
	public TextTranslationBridgeController textTranslationBridgeController(
			TextTranslationController textTranslationController, TextToTranslateDtoMapper textToTranslateDtoMapper) {
		return new TextTranslationBridgeController(textTranslationController, textToTranslateDtoMapper);
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

}
