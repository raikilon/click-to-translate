package ch.clicktotranslate.segment.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public final class Segment {

	private final String word;

	private final String sentence;

	private final String sourceLanguage;

	private final String targetLanguage;

	private final String translatedWord;

	private final String translatedSentence;

	public Segment(String word, String sentence, String sourceLanguage, String targetLanguage) {
		this(word, sentence, sourceLanguage, targetLanguage, null, null);
	}

	private Segment(String word, String sentence, String sourceLanguage, String targetLanguage, String translatedWord,
			String translatedSentence) {
		if (isMissing(word) || isMissing(sourceLanguage) || isMissing(targetLanguage)) {
			throw new IllegalArgumentException("Invalid segment parameters.");
		}
		if (translatedWord != null && isMissing(translatedWord)) {
			throw new IllegalArgumentException("Invalid segment parameters.");
		}
		this.word = word;
		this.sentence = sentence;
		this.sourceLanguage = sourceLanguage;
		this.targetLanguage = targetLanguage;
		this.translatedWord = translatedWord;
		this.translatedSentence = translatedSentence;
	}

	public Segment withTranslations(String word, String sentence) {
		if (isMissing(word)) {
			throw new IllegalStateException("Segment translation failed.");
		}

		return new Segment(this.word, this.sentence, this.sourceLanguage, this.targetLanguage, word, sentence);
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
