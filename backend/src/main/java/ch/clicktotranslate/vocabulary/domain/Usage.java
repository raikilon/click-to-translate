package ch.clicktotranslate.vocabulary.domain;

import java.time.Instant;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.ddd.types.ValueObject;

public class Usage implements Entity<Entry, Usage.Id> {

	private final Id id;

	private final String sentence;

	private final TextSpan sentenceSpan;

	private final String translation;

	private final TextSpan translationSpan;

	private final Language language;

	private final Instant lastEdit;

	private final Instant createdAt;

	public Usage(Id id, String sentence, TextSpan sentenceSpan, String translation, TextSpan translationSpan,
				 Language language, Instant lastEdit, Instant createdAt) {
		this.id = id;
		this.sentence = requireSentence(sentence);
		this.sentenceSpan = requireSpan(sentenceSpan, this.sentence, "sentence");
		this.translation = requireTranslation(translation);
		this.translationSpan = requireSpan(translationSpan, this.translation, "translation");
		this.language = requireTargetLanguage(language);
		if (lastEdit == null) {
			throw new IllegalArgumentException("lastEdit must not be null");
		}
		if (createdAt == null) {
			throw new IllegalArgumentException("createdAt must not be null");
		}
		this.lastEdit = lastEdit;
		this.createdAt = createdAt;
	}

	public Usage(String sentence, String sentenceFragment, String translation, String translationFragment,
			Language language) {
		this(null, sentence, TextSpan.find(sentence, sentenceFragment), translation,
				TextSpan.find(translation, translationFragment), language, Instant.now(), Instant.now());
	}

	@Override
	public Id getId() {
		return id;
	}

	public Id id() {
		return id;
	}

	public String sentence() {
		return sentence;
	}

	public TextSpan sentenceSpan() {
		return sentenceSpan;
	}

	public String translation() {
		return translation;
	}

	public TextSpan translationSpan() {
		return translationSpan;
	}

	public Language targetLanguage() {
		return language;
	}

	public Instant lastEdit() {
		return lastEdit;
	}

	public Instant createdAt() {
		return createdAt;
	}

	private static String requireSentence(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("sentence must not be blank");
		}
		return value.trim();
	}

	private static String requireTranslation(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("translation must not be blank");
		}
		return value.trim();
	}

	private static Language requireTargetLanguage(Language value) {
		if (value == null) {
			throw new IllegalArgumentException("language must not be null");
		}
		return value;
	}

	private static TextSpan requireSpan(TextSpan span, String text, String name) {
		if (span == null) {
			throw new IllegalArgumentException(name + " span must not be null");
		}
		if (span.end() > text.length()) {
			throw new IllegalArgumentException(name + " span out of bounds");
		}
		return span;
	}

	public record Id(Long value) implements Identifier {

		public static Id of(Long value) {
			return new Id(value);
		}

		public Id {
			if (value == null || value <= 0) {
				throw new IllegalArgumentException("usageId must be > 0");
			}
		}

	}

}

