package ch.clicktotranslate.vocabulary.domain;

import java.util.Locale;
import org.jmolecules.ddd.types.ValueObject;

public record Term(Language language, String term) implements ValueObject {

	public Term {
		requireLanguage(language);
		term = requireTerm(term);
	}

	private static void requireLanguage(Language value) {
		if (value == null) {
			throw new IllegalArgumentException("language must not be null");
		}
	}

	private static String requireTerm(String value) {
		if (value == null) {
			throw new IllegalArgumentException("term must not be null");
		}
		String normalized = value.trim().toLowerCase(Locale.ROOT);
		if (normalized.isEmpty()) {
			throw new IllegalArgumentException("term must not be blank");
		}
		return normalized;
	}

}

