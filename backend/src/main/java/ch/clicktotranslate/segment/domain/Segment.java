package ch.clicktotranslate.segment.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public final class Segment {

	private final String word;

	private final String sentence;

	private final String sourceLanguage;

	private final String targetLanguage;

	private String translatedWord;

	private String translatedSentence;

	public Segment(String word, String sentence, String sourceLanguage, String targetLanguage) {
		if (isMissing(word) || isMissing(sourceLanguage) || isMissing(targetLanguage)) {
			throw new IllegalArgumentException("Invalid segment parameters.");
		}
		this.word = word;
		this.sentence = sentence;
		this.sourceLanguage = sourceLanguage;
		this.targetLanguage = targetLanguage;
	}

	public void setTranslations(String word, String sentence) {
		if (isMissing(word)) {
			throw new IllegalArgumentException("Invalid segment parameters.");
		}

		this.translatedWord = word;
		this.translatedSentence = sentence;
	}

	private static boolean isMissing(String s) {
		return s == null || s.isBlank();
	}

	public String word() {
		return word;
	}

	public String sentence() {
		return sentence;
	}

	public String sourceLanguage() {
		return sourceLanguage;
	}

	public String targetLanguage() {
		return targetLanguage;
	}

	public String translatedSentence() {
		return translatedSentence;
	}

	public String translatedWord() {
		return translatedWord;
	}

}
