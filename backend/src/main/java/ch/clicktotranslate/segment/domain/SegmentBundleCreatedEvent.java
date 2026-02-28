package ch.clicktotranslate.segment.domain;

import org.jmolecules.event.annotation.DomainEvent;
import org.springframework.modulith.NamedInterface;

import java.time.Instant;
import java.util.Objects;

@NamedInterface("domain")
@DomainEvent
public class SegmentBundleCreatedEvent {

	private final String userId;

	private final String word;

	private final String sentence;

	private final String wordTranslation;

	private final String sentenceTranslation;

	private final String sourceLanguage;

	private final String targetLanguage;

	private final Instant occurredAt;

	public SegmentBundleCreatedEvent(String userId, String word, String sentence, String wordTranslation,
			String sentenceTranslation, String sourceLanguage, String targetLanguage, Instant occurredAt) {

		if (isMissing(userId) || isMissing(word) || isMissing(sourceLanguage) || isMissing(targetLanguage)) {
			throw new IllegalArgumentException("Invalid segment bundle created event parameters.");
		}
		this.userId = userId;
		this.word = word;
		this.sentence = sentence;
		this.wordTranslation = wordTranslation;
		this.sentenceTranslation = sentenceTranslation;
		this.sourceLanguage = sourceLanguage;
		this.targetLanguage = targetLanguage;
		this.occurredAt = occurredAt;
	}

	public String userId() {
		return userId;
	}

	public String word() {
		return word;
	}

	public String sentence() {
		return sentence;
	}

	public String wordTranslation() {
		return wordTranslation;
	}

	public String sentenceTranslation() {
		return sentenceTranslation;
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
		var that = (SegmentBundleCreatedEvent) obj;
		return Objects.equals(this.userId, that.userId) && Objects.equals(this.word, that.word)
				&& Objects.equals(this.sentence, that.sentence)
				&& Objects.equals(this.wordTranslation, that.wordTranslation)
				&& Objects.equals(this.sentenceTranslation, that.sentenceTranslation)
				&& Objects.equals(this.sourceLanguage, that.sourceLanguage)
				&& Objects.equals(this.targetLanguage, that.targetLanguage)
				&& Objects.equals(this.occurredAt, that.occurredAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, word, sentence, wordTranslation, sentenceTranslation, sourceLanguage,
				targetLanguage, occurredAt);
	}

	private static boolean isMissing(String s) {
		return s == null || s.isBlank();
	}

}
