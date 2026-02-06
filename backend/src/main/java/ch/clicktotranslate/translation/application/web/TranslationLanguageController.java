package ch.clicktotranslate.translation.application.web;

import java.util.List;

import ch.clicktotranslate.translation.domain.Language;

public class TranslationLanguageController {

	public List<Language> listLanguages() {
		return List.of(Language.values());
	}

}
