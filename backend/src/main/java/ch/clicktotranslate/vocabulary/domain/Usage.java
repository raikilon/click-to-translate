package ch.clicktotranslate.vocabulary.domain;

import java.time.Instant;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;

public class Usage implements Entity<Entry, Usage.Id> {

	private final Id id;

	private final String sentence;

	private final TextSpan sentenceSpan;

	private final String translation;

	private final TextSpan translationSpan;

	private final Language language;

	private boolean starred;

	private final Instant lastEdit;

	private final Instant createdAt;

	public Usage(Id id, String sentence, TextSpan sentenceSpan, String translation, TextSpan translationSpan,
			Language language, boolean starred, Instant lastEdit, Instant createdAt) {
		this.id = id;
		this.sentence = requireSentence(sentence);
		this.sentenceSpan = requireSpan(sentenceSpan, "sentence");
		this.translation = requireTranslation(translation);
		this.translationSpan = translationSpan;
		this.language = requireLanguage(language);
		this.starred = starred;
		if (lastEdit == null) {
			throw new IllegalArgumentException("lastEdit must not be null");
		}
		if (createdAt == null) {
			throw new IllegalArgumentException("createdAt must not be null");
		}
		this.lastEdit = lastEdit;
		this.createdAt = createdAt;
	}

	public Usage(String sentence, String word, String translation, String wordTranslation, Language language) {
		this(null, sentence, TextSpan.find(sentence, word), translation,
				findTranslationSpan(translation, wordTranslation), language, false, Instant.now(), Instant.now());
	}

	public Usage(Id id, String sentence, TextSpan sentenceSpan, String translation, TextSpan translationSpan,
			Language language, Instant lastEdit, Instant createdAt) {
		this(id, sentence, sentenceSpan, translation, translationSpan, language, false, lastEdit, createdAt);
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

	public Language language() {
		return language;
	}

	public boolean starred() {
		return starred;
	}

	public Instant lastEdit() {
		return lastEdit;
	}

	public Instant createdAt() {
		return createdAt;
	}

	public void star() {
		this.starred = true;
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

	private static Language requireLanguage(Language value) {
		if (value == null) {
			throw new IllegalArgumentException("language must not be null");
		}
		return value;
	}

	private static TextSpan requireSpan(TextSpan span, String name) {
		if (span == null) {
			throw new IllegalArgumentException(name + " span must not be null");
		}
		return span;
	}

	private static TextSpan findTranslationSpan(String translation, String wordTranslation) {
		try {
			return TextSpan.find(translation, wordTranslation);
		}
		catch (IllegalArgumentException ignored) {
			return null;
		}
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
