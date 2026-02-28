package ch.clicktotranslate.vocabulary.domain;

import ch.clicktotranslate.auth.UserId;

public record SegmentBundle(UserId userId, String term, String word, String sentence, String termTranslation,
		String wordTranslation, String sentenceTranslation, Language sourceLanguage, Language targetLanguage) {

	public SegmentBundle {
		if (userId == null || isMissing(term) || isMissing(termTranslation) || sourceLanguage == null
				|| targetLanguage == null) {
			throw new IllegalArgumentException("Invalid segment bundle parameters.");
		}
	}

	public SegmentBundle(UserId userId, String term, String word, String sentence, String termTranslation,
			String wordTranslation, String sentenceTranslation, String sourceLanguage, String targetLanguage) {
		this(userId, term, word, sentence, termTranslation, wordTranslation, sentenceTranslation,
				toLanguage(sourceLanguage), toLanguage(targetLanguage));
	}

	private static Language toLanguage(String value) {
		if (isMissing(value)) {
			throw new IllegalArgumentException("Invalid segment bundle parameters.");
		}
		try {
			return Language.valueOf(value);
		}
		catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Invalid language: " + value, ex);
		}
	}

	private static boolean isMissing(String value) {
		return value == null || value.isBlank();
	}
}
