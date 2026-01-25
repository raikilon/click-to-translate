package ch.clicktotranslate.vocabulary.infrastructure.event;

import ch.clicktotranslate.vocabulary.domain.usecase.RegisterUsageFromTranslation;
import ch.clicktotranslate.vocabulary.domain.usecase.model.RegisterUsageInput;

public class TranslatedWordEventHandler {
	private final RegisterUsageFromTranslation registerUsageFromTranslation;

	public TranslatedWordEventHandler(RegisterUsageFromTranslation registerUsageFromTranslation) {
		this.registerUsageFromTranslation = registerUsageFromTranslation;
	}

	public void handle(RegisterUsageInput input) {
		registerUsageFromTranslation.execute(input);
	}
}
