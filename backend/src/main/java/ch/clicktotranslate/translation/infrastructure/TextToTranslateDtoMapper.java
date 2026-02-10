package ch.clicktotranslate.translation.infrastructure;

import ch.clicktotranslate.translation.domain.TextToTranslate;

public class TextToTranslateDtoMapper {

	public TextToTranslate toDomain(TextToTranslateDto request) {
		if (request == null) {
			return null;
		}
		return new TextToTranslate(request.text(), toLanguage(request.sourceLanguage()),
				toLanguage(request.targetLanguage()));
	}

	public TextToTranslateDto toDto(TextToTranslate request) {
		if (request == null) {
			return null;
		}
		return new TextToTranslateDto(request.text(), toLanguageDto(request.sourceLanguage()),
				toLanguageDto(request.targetLanguage()));
	}

	private String toLanguage(LanguageDto language) {
		if (language == null) {
			return null;
		}
		return language.name();
	}

	private LanguageDto toLanguageDto(String language) {
		if (language == null) {
			return null;
		}
		return LanguageDto.valueOf(language);
	}

}
