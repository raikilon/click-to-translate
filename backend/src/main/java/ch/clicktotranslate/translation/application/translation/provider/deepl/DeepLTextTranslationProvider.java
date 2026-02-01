package ch.clicktotranslate.translation.application.translation.provider.deepl;

import ch.clicktotranslate.translation.domain.TextToTranslate;
import ch.clicktotranslate.translation.application.translation.provider.TextTranslationProviderType;
import ch.clicktotranslate.translation.application.translation.TextTranslationProvider;

public class DeepLTextTranslationProvider implements TextTranslationProvider {

	private final DeepLTextTranslation deepLTextTranslation;

	private final DeepLLanguageMapper languageMapper;

	public DeepLTextTranslationProvider(DeepLTextTranslation deepLTextTranslation, DeepLLanguageMapper languageMapper) {
		this.deepLTextTranslation = deepLTextTranslation;
		this.languageMapper = languageMapper;
	}

	@Override
	public String translate(TextToTranslate request) {
		return deepLTextTranslation.translate(request.text(), languageMapper.toDeepLCode(request.sourceLanguage()),
				languageMapper.toDeepLCode(request.targetLanguage()));
	}

	@Override
	public TextTranslationProviderType getProvider() {
		return TextTranslationProviderType.DEEPL;
	}

}
