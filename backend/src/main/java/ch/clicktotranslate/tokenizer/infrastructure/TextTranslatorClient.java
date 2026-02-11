package ch.clicktotranslate.tokenizer.infrastructure;

import ch.clicktotranslate.tokenizer.application.TextTranslator;
import ch.clicktotranslate.translation.infrastructure.LanguageDto;
import ch.clicktotranslate.translation.infrastructure.TextTranslationFacade;
import ch.clicktotranslate.translation.infrastructure.TextToTranslateDto;

public class TextTranslatorClient implements TextTranslator {

	private final TextTranslationFacade textTranslationFacade;

	public TextTranslatorClient(TextTranslationFacade textTranslationFacade) {
		this.textTranslationFacade = textTranslationFacade;
	}

	@Override
	public String translate(String text, String sourceLanguage, String targetLanguage) {
		TextToTranslateDto request = new TextToTranslateDto(text, LanguageDto.fromString(sourceLanguage),
				LanguageDto.fromString(targetLanguage));
		return textTranslationFacade.translate(request);
	}

}
