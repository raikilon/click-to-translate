package ch.clicktotranslate.translation.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public final class TextToTranslate {

	private final String text;

	private final String sourceLanguage;

	private final String targetLanguage;

	public TextToTranslate(String text, String sourceLanguage, String targetLanguage) {
		if (isMissing(text) || isMissing(sourceLanguage) || isMissing(targetLanguage)) {
			throw new IllegalArgumentException("Invalid text to translate parameters.");
		}
		this.text = text;
		this.sourceLanguage = sourceLanguage;
		this.targetLanguage = targetLanguage;
	}

	public String text() {
		return text;
	}

	public String sourceLanguage() {
		return sourceLanguage;
	}

	public String targetLanguage() {
		return targetLanguage;
	}

	private static boolean isMissing(String value) {
		return value == null || value.isBlank();
	}

}
