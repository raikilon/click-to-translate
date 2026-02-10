package ch.clicktotranslate.segment.infrastructure;

import ch.clicktotranslate.segment.domain.TextTranslator;
import ch.clicktotranslate.translation.infrastructure.LanguageDto;
import ch.clicktotranslate.translation.infrastructure.TextTranslationBridgeController;
import ch.clicktotranslate.translation.infrastructure.TextToTranslateDto;

public class TextTranslatorBridge implements TextTranslator {

	private final TextTranslationBridgeController textTranslationBridgeController;

	public TextTranslatorBridge(TextTranslationBridgeController textTranslationBridgeController) {
		this.textTranslationBridgeController = textTranslationBridgeController;
	}

	@Override
	public String translate(String text, String sourceLanguage, String targetLanguage) {
		TextToTranslateDto request = new TextToTranslateDto(text, LanguageDto.fromString(sourceLanguage),
				LanguageDto.fromString(targetLanguage));
		return textTranslationBridgeController.translate(request);
	}

}
