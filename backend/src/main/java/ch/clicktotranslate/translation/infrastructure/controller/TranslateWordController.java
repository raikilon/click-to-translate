package ch.clicktotranslate.translation.infrastructure.controller;

import ch.clicktotranslate.translation.domain.usecase.TranslateWord;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordInput;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordOutput;

public class TranslateWordController {
	private final TranslateWord translateWord;

	public TranslateWordController(TranslateWord translateWord) {
		this.translateWord = translateWord;
	}

	public TranslateWordOutput translate(TranslateWordInput input) {
		return translateWord.execute(input);
	}
}
