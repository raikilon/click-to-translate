package ch.clicktotranslate.translation.domain.outbound;

import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordInput;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordOutput;

public interface TranslationGateway {
	TranslateWordOutput translate(TranslateWordInput input);
}
