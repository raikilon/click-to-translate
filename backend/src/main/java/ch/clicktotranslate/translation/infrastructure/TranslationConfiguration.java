package ch.clicktotranslate.translation.infrastructure;

import com.deepl.api.DeepLClient;
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
	public TextTranslationFacade textTranslationBridgeController(TextTranslationController textTranslationController,
			TextToTranslateDtoMapper textToTranslateDtoMapper) {
		return new TextTranslationFacade(textTranslationController, textToTranslateDtoMapper);
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
	public DeepLClient deepLClient(@Value("${deepl.auth-key}") String authKey) {
		if (authKey == null || authKey.isBlank()) {
			throw new IllegalArgumentException("DeepL auth key is required.");
		}
		return new DeepLClient(authKey);
	}

	@Bean
	public DeepLTextTranslation deepLApiClient(DeepLClient deepLClient) {
		return new DeepLTextTranslationClient(deepLClient);
	}

	@Bean
	public TextTranslationProvider deepLTranslationStrategy(DeepLTextTranslation apiClient,
			DeepLLanguageMapper languageMapper) {
		return new DeepLTextTranslationProvider(apiClient, languageMapper);
	}

}
