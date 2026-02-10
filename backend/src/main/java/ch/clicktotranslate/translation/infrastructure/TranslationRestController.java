package ch.clicktotranslate.translation.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.clicktotranslate.translation.application.TranslationLanguageController;
import ch.clicktotranslate.translation.application.TextTranslationController;
import ch.clicktotranslate.translation.domain.TextToTranslate;

@RestController
@RequestMapping("/api/translate")
public class TranslationRestController {

	private final TranslationLanguageController translationLanguageController;

	private final TextTranslationController textTranslationController;

	private final LanguageDtoMapper languageDtoMapper;

	private final TextToTranslateDtoMapper textToTranslateDtoMapper;

	public TranslationRestController(TranslationLanguageController translationLanguageController,
			TextTranslationController textTranslationController, LanguageDtoMapper languageDtoMapper,
			TextToTranslateDtoMapper textToTranslateDtoMapper) {
		this.translationLanguageController = translationLanguageController;
		this.textTranslationController = textTranslationController;
		this.languageDtoMapper = languageDtoMapper;
		this.textToTranslateDtoMapper = textToTranslateDtoMapper;
	}

	@GetMapping("/languages")
	public List<LanguageDto> listLanguages() {
		return translationLanguageController.listLanguages()
			.stream()
			.map(languageDtoMapper::toDto)
			.collect(Collectors.toList());
	}

	@PostMapping("/text")
	public String translate(@RequestBody TextToTranslateDto request) {
		TextToTranslate toTranslate = textToTranslateDtoMapper.toDomain(request);
		return textTranslationController.translate(toTranslate);
	}

}
