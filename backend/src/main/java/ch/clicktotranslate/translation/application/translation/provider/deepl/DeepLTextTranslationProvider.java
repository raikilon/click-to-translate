package ch.clicktotranslate.translation.application.translation.provider.deepl;

import ch.clicktotranslate.translation.domain.TextToTranslate;
import ch.clicktotranslate.translation.application.translation.provider.TextTranslationProviderType;
import ch.clicktotranslate.translation.application.translation.TextTranslationProvider;

public class DeepLTextTranslationProvider implements TextTranslationProvider {

	private final DeepLTextTranslation deepLTextTranslation;

	public DeepLTextTranslationProvider(DeepLTextTranslation deepLTextTranslation) {
		this.deepLTextTranslation = deepLTextTranslation;
	}

	@Override
	public String translate(TextToTranslate request) {
		return deepLTextTranslation.translate(request);
	}

	@Override
	public TextTranslationProviderType getProvider() {
		return TextTranslationProviderType.DEEPL;
	}

}
