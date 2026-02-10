package ch.clicktotranslate.translation.infrastructure;

import ch.clicktotranslate.translation.application.TextTranslationController;
import ch.clicktotranslate.translation.domain.TextToTranslate;
import org.springframework.modulith.NamedInterface;

@NamedInterface
public class TextTranslationBridgeController {

	private final TextTranslationController textTranslationController;

	private final TextToTranslateDtoMapper textToTranslateDtoMapper;

	public TextTranslationBridgeController(TextTranslationController textTranslationController,
			TextToTranslateDtoMapper textToTranslateDtoMapper) {
		this.textTranslationController = textTranslationController;
		this.textToTranslateDtoMapper = textToTranslateDtoMapper;
	}

	public String translate(TextToTranslateDto request) {
		TextToTranslate toTranslate = textToTranslateDtoMapper.toDomain(request);
		return textTranslationController.translate(toTranslate);
	}

}
