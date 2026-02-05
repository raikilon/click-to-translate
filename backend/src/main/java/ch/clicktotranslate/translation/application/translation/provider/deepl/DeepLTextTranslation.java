package ch.clicktotranslate.translation.application.translation.provider.deepl;

import ch.clicktotranslate.translation.domain.TextToTranslate;

public interface DeepLTextTranslation {

	String translate(TextToTranslate request);

}
