package ch.clicktotranslate.translation.infrastructure;

import ch.clicktotranslate.translation.domain.Language;

public class LanguageDtoMapper {

	public LanguageDto toDto(Language language) {
		return LanguageDto.valueOf(language.name());
	}

	public Language toDomain(LanguageDto languageDto) {
		return Language.valueOf(languageDto.name());
	}

}
