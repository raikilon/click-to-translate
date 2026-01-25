package ch.clicktotranslate.translation.infrastructure.gateway.client;

import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordInput;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordOutput;

public interface DeepLApiClient {
	TranslateWordOutput translate(TranslateWordInput request);
}
