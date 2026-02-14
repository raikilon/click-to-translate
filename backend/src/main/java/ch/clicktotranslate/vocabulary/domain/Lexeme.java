package ch.clicktotranslate.vocabulary.domain;

import java.util.Locale;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

@Entity
public class Lexeme {

	@Identity
	private final Long id;

	private final Language language;

	private final String lemma;

	public Lexeme(Long id, Language language, String lemma) {
		this.id = id;
		this.language = requireLanguage(language);
		this.lemma = normalizeLemma(lemma);
	}

	public Lexeme(Language language, String lemma) {
		this(null, language, lemma);
	}

	public Long id() {
		return id;
	}

	public Language language() {
		return language;
	}

	public String lemma() {
		return lemma;
	}

	private static Language requireLanguage(Language value) {
		if (value == null) {
			throw new IllegalArgumentException("language must not be null");
		}
		return value;
	}

	private static String normalizeLemma(String value) {
		if (value == null) {
			throw new IllegalArgumentException("lemma must not be null");
		}
		String normalized = value.trim().toLowerCase(Locale.ROOT);
		if (normalized.isEmpty()) {
			throw new IllegalArgumentException("lemma must not be blank");
		}
		return normalized;
	}

}
