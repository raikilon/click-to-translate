package ch.clicktotranslate.lemmatizer.domain;

import java.time.Instant;
import java.util.Objects;

import org.jmolecules.event.annotation.DomainEvent;
import org.springframework.modulith.NamedInterface;

@NamedInterface("domain")
@DomainEvent
public class SegmentBundleLemmatizedEvent {

	private final String userId;

	private final String lemmatizedWord;

	private final String lemmatizedWordTranslation;

	private final String sentence;

	private final String sentenceTranslation;

	private final String word;

	private final String wordTranslation;

	private final String sourceLanguage;

	private final String targetLanguage;

	private final Instant occurredAt;

	public SegmentBundleLemmatizedEvent(String userId, String lemmatizedWord, String lemmatizedWordTranslation,
			String sentence, String sentenceTranslation, String word, String wordTranslation, String sourceLanguage,
			String targetLanguage, Instant occurredAt) {

		if (isMissing(userId) || isMissing(lemmatizedWord) || isMissing(lemmatizedWordTranslation)
				|| isMissing(sourceLanguage) || isMissing(targetLanguage)) {
			throw new IllegalArgumentException("Invalid segment bundle lemmatizedWord event parameters.");
		}

		this.userId = userId;
		this.lemmatizedWord = lemmatizedWord;
		this.lemmatizedWordTranslation = lemmatizedWordTranslation;
		this.sentence = sentence;
		this.sentenceTranslation = sentenceTranslation;
		this.word = word;
		this.wordTranslation = wordTranslation;
		this.sourceLanguage = sourceLanguage;
		this.targetLanguage = targetLanguage;
		this.occurredAt = occurredAt;
	}

	public String userId() {
		return userId;
	}

	public String lemmatizedWord() {
		return lemmatizedWord;
	}

	public String lemmatizedWordTranslation() {
		return lemmatizedWordTranslation;
	}

	public String sentence() {
		return sentence;
	}

	public String sentenceTranslation() {
		return sentenceTranslation;
	}

	public String word() {
		return word;
	}

	public String wordTranslation() {
		return wordTranslation;
	}

	public String sourceLanguage() {
		return sourceLanguage;
	}

	public String targetLanguage() {
		return targetLanguage;
	}

	public Instant occurredAt() {
		return occurredAt;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		var that = (SegmentBundleLemmatizedEvent) obj;
		return Objects.equals(this.userId, that.userId) && Objects.equals(this.lemmatizedWord, that.lemmatizedWord)
				&& Objects.equals(this.lemmatizedWordTranslation, that.lemmatizedWordTranslation)
				&& Objects.equals(this.sentence, that.sentence)
				&& Objects.equals(this.sentenceTranslation, that.sentenceTranslation)
				&& Objects.equals(this.word, that.word) && Objects.equals(this.wordTranslation, that.wordTranslation)
				&& Objects.equals(this.sourceLanguage, that.sourceLanguage)
				&& Objects.equals(this.targetLanguage, that.targetLanguage)
				&& Objects.equals(this.occurredAt, that.occurredAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, lemmatizedWord, lemmatizedWordTranslation, sentence, sentenceTranslation, word,
				wordTranslation, sourceLanguage, targetLanguage, occurredAt);
	}

	private static boolean isMissing(String s) {
		return s == null || s.isBlank();
	}

}
