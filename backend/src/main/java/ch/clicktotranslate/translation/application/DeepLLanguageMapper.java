package ch.clicktotranslate.translation.application;

import ch.clicktotranslate.translation.domain.Language;

public class DeepLLanguageMapper {

	public String toDeepLCode(Language language) {
		return language.name();
	}

}
