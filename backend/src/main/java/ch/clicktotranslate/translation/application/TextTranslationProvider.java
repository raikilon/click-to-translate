package ch.clicktotranslate.translation.application;

import ch.clicktotranslate.translation.domain.TextToTranslate;

public interface TextTranslationProvider {

	String translate(TextToTranslate request);

	TextTranslationProviderType getProvider();

}
