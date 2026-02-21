package ch.clicktotranslate.tokenizer.domain;

import java.time.Instant;
import java.util.Objects;

import org.jmolecules.event.annotation.DomainEvent;
import org.springframework.modulith.NamedInterface;

@NamedInterface
@DomainEvent
public class SegmentBundleTokenizedEvent {

	private final String userId;

	private final String tokenizedWord;

	private final String tokenizedWordTranslation;

	private final String sentence;

	private final String sentenceTranslation;

	private final String word;

	private final String wordTranslation;

	private final String sourceLanguage;

	private final String targetLanguage;

	private final Instant occurredAt;

	public SegmentBundleTokenizedEvent(String userId, String tokenizedWord, String tokenizedWordTranslation,
			String sentence, String sentenceTranslation, String word, String wordTranslation, String sourceLanguage,
			String targetLanguage, Instant occurredAt) {

		if (isMissing(userId) || isMissing(tokenizedWord) || isMissing(tokenizedWordTranslation)
				|| isMissing(sourceLanguage) || isMissing(targetLanguage)) {
			throw new IllegalArgumentException("Invalid segment bundle tokenizedWord event parameters.");
		}

		this.userId = userId;
		this.tokenizedWord = tokenizedWord;
		this.tokenizedWordTranslation = tokenizedWordTranslation;
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

	public String tokenizedWord() {
		return tokenizedWord;
	}

	public String tokenizedWordTranslation() {
		return tokenizedWordTranslation;
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
		var that = (SegmentBundleTokenizedEvent) obj;
		return Objects.equals(this.userId, that.userId) && Objects.equals(this.tokenizedWord, that.tokenizedWord)
				&& Objects.equals(this.tokenizedWordTranslation, that.tokenizedWordTranslation)
				&& Objects.equals(this.sentence, that.sentence)
				&& Objects.equals(this.sentenceTranslation, that.sentenceTranslation)
				&& Objects.equals(this.word, that.word) && Objects.equals(this.wordTranslation, that.wordTranslation)
				&& Objects.equals(this.sourceLanguage, that.sourceLanguage)
				&& Objects.equals(this.targetLanguage, that.targetLanguage)
				&& Objects.equals(this.occurredAt, that.occurredAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, tokenizedWord, tokenizedWordTranslation, sentence, sentenceTranslation, word,
				wordTranslation, sourceLanguage, targetLanguage, occurredAt);
	}

	@Override
	public String toString() {
		return "SegmentBundleTokenizedEvent[" + "userId=" + userId + ", " + "tokenizedWord=" + tokenizedWord + ", "
				+ "tokenizedWordTranslation=" + tokenizedWordTranslation + ", " + "sentence=" + sentence + ", "
				+ "sentenceTranslation=" + sentenceTranslation + ", " + "word=" + word + ", " + "wordTranslation="
				+ wordTranslation + ", " + "sourceLanguage=" + sourceLanguage + ", " + "language=" + targetLanguage
				+ ", " + "occurredAt=" + occurredAt + ']';
	}

	private static boolean isMissing(String s) {
		return s == null || s.isBlank();
	}

}
