package ch.clicktotranslate.translation.application;

import ch.clicktotranslate.translation.domain.TextToTranslate;
import ch.clicktotranslate.translation.domain.Language;

public class DeepLTextTranslationProvider implements TextTranslationProvider {

	private final DeepLTextTranslation deepLTextTranslation;

	private final DeepLLanguageMapper languageMapper;

	public DeepLTextTranslationProvider(DeepLTextTranslation deepLTextTranslation, DeepLLanguageMapper languageMapper) {
		this.deepLTextTranslation = deepLTextTranslation;
		this.languageMapper = languageMapper;
	}

	@Override
	public String translate(TextToTranslate request) {
		Language sourceLanguage = toLanguage(request.sourceLanguage());
		Language targetLanguage = toLanguage(request.targetLanguage());
		return deepLTextTranslation.translate(request.text(), languageMapper.toDeepLCode(sourceLanguage),
				languageMapper.toDeepLCode(targetLanguage));
	}

	@Override
	public TextTranslationProviderType getProvider() {
		return TextTranslationProviderType.DEEPL;
	}

	private Language toLanguage(String language) {
		if (language == null) {
			return null;
		}
		return Language.valueOf(language);
	}

}
