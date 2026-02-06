package ch.clicktotranslate.translation.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public enum Language {

	GERMAN("de"), ENGLISH("en"), SPANISH("es"), FRENCH("fr"), ITALIAN("it"), NORWEGIAN("nb"), DANISH("da"),
	ESTONIAN("et"), FINNISH("fi"), PORTUGUESE("pt"), SLOVENIAN("sl"), SWEDISH("sv");

	private final String code;

	Language(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
