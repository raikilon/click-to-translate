package ch.clicktotranslate.segment.domain;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

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

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		var that = (Segment) obj;
		return Objects.equals(this.word, that.word) && Objects.equals(this.sentence, that.sentence)
				&& Objects.equals(this.sourceLanguage, that.sourceLanguage)
				&& Objects.equals(this.targetLanguage, that.targetLanguage);
	}

	@Override
	public int hashCode() {
		return Objects.hash(word, sentence, sourceLanguage, targetLanguage);
	}

	@Override
	public String toString() {
		return "Segment[" + "word=" + word + ", " + "sentence=" + sentence + ", " + "sourceLanguage=" + sourceLanguage
				+ ", " + "language=" + targetLanguage + ']';
	}

	public String translatedSentence() {
		return translatedSentence;
	}

	public String translatedWord() {
		return translatedWord;
	}

}
