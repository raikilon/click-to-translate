package ch.clicktotranslate.translation.application.translation;

import ch.clicktotranslate.translation.domain.TextToTranslate;
import ch.clicktotranslate.translation.application.translation.provider.TextTranslationProviderType;

public interface TextTranslationProvider {

	String translate(TextToTranslate request);

	TextTranslationProviderType getProvider();

}
