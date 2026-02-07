package ch.clicktotranslate.translation.infrastructure.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.clicktotranslate.translation.application.web.TranslationLanguageController;

@RestController
@RequestMapping("/api/translate/languages")
public class TranslationLanguagesRestController {

	private final TranslationLanguageController translationLanguageController;

	private final LanguageDtoMapper languageDtoMapper;

	public TranslationLanguagesRestController(TranslationLanguageController translationLanguageController,
			LanguageDtoMapper languageDtoMapper) {
		this.translationLanguageController = translationLanguageController;
		this.languageDtoMapper = languageDtoMapper;
	}

	@GetMapping
	public List<LanguageDto> listLanguages() {
		return translationLanguageController.listLanguages()
			.stream()
			.map(languageDtoMapper::toDto)
			.collect(Collectors.toList());
	}

}
