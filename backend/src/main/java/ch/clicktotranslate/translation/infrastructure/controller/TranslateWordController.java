package ch.clicktotranslate.translation.infrastructure.controller;

import ch.clicktotranslate.translation.domain.usecase.TranslateWordUseCase;
import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.domain.entity.TranslatedWord;

public class TranslateWordController {
	private final TranslateWordUseCase translateWordUseCase;

	public TranslateWordController(TranslateWordUseCase translateWordUseCase) {
		this.translateWordUseCase = translateWordUseCase;
	}

	public TranslatedWord translate(TranslateWord input) {
		return translateWordUseCase.execute(input);
	}
}
