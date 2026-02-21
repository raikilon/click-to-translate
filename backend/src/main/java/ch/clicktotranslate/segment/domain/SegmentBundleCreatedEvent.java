package ch.clicktotranslate.segment.domain;

import org.jmolecules.event.annotation.DomainEvent;
import org.springframework.modulith.NamedInterface;

import java.time.Instant;
import java.util.Objects;

@NamedInterface
@DomainEvent
public class SegmentBundleCreatedEvent {

	private final String userId;

	private final String word;

	private final String sentence;

	private final String wordTranslation;

	private final String sentenceTranslation;

	private final String sourceLanguage;

	private final String targetLanguage;

	private final Source source;

	private final SourceMetadata sourceMetadata;

	private final Instant occurredAt;

	public SegmentBundleCreatedEvent(String userId, String word, String sentence, String wordTranslation,
			String sentenceTranslation, String sourceLanguage, String targetLanguage, Source source,
			SourceMetadata sourceMetadata, Instant occurredAt) {

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
		this.source = source;
		this.sourceMetadata = sourceMetadata;
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

	public Source source() {
		return source;
	}

	public SourceMetadata sourceMetadata() {
		return sourceMetadata;
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
				&& Objects.equals(this.targetLanguage, that.targetLanguage) && Objects.equals(this.source, that.source)
				&& Objects.equals(this.sourceMetadata, that.sourceMetadata)
				&& Objects.equals(this.occurredAt, that.occurredAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, word, sentence, wordTranslation, sentenceTranslation, sourceLanguage,
				targetLanguage, source, sourceMetadata, occurredAt);
	}

	@Override
	public String toString() {
		return "SegmentBundleCreatedEvent[" + "userId=" + userId + ", " + "word=" + word + ", " + "sentence=" + sentence
				+ ", " + "wordTranslation=" + wordTranslation + ", " + "sentenceTranslation=" + sentenceTranslation
				+ ", " + "sourceLanguage=" + sourceLanguage + ", " + "language=" + targetLanguage + ", " + "source="
				+ source + ", " + "sourceMetadata=" + sourceMetadata + ", " + "occurredAt=" + occurredAt + ']';
	}

	private static boolean isMissing(String s) {
		return s == null || s.isBlank();
	}

	public record Source(String type, String id, String title) {
	}

	public interface SourceMetadata {

	}

	public record GenericSourceMetadata(String url, String domain, Integer selectionOffset,
			Integer paragraphIndex) implements SourceMetadata {
	}

	public record YoutubeSourceMetadata(String url, String domain, String videoId,
			Integer timestampSeconds) implements SourceMetadata {
	}

}
