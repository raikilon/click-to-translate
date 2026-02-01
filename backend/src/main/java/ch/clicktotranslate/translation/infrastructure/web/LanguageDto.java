package ch.clicktotranslate.translation.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum LanguageDto {

	DE, EN, ES, FR, IT, NB, DA, ET, FI, PT, SL, SV;

	@JsonValue
	public String toJson() {
		return name().toLowerCase(Locale.ENGLISH);
	}

	@JsonCreator
	public static LanguageDto fromJson(String value) {
		return LanguageDto.valueOf(value.toUpperCase(Locale.ENGLISH));
	}

}
