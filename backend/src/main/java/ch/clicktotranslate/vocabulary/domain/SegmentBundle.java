package ch.clicktotranslate.vocabulary.domain;

public record SegmentBundle(UserId userId, String lemmatizedWord, String word, String sentence,
		String lemmatizedWordTranslation, String wordTranslation, String sentenceTranslation, Language sourceLanguage,
		Language targetLanguage) {

	public SegmentBundle {
		if (userId == null || isMissing(lemmatizedWord) || isMissing(lemmatizedWordTranslation)
				|| sourceLanguage == null || targetLanguage == null) {
			throw new IllegalArgumentException("Invalid segment bundle parameters.");
		}
	}

	public SegmentBundle(UserId userId, String lemmatized, String word, String sentence, String lemmatizedTranslation,
			String wordTranslation, String sentenceTranslation, String sourceLanguage, String targetLanguage) {
		this(userId, lemmatized, word, sentence, lemmatizedTranslation, wordTranslation, sentenceTranslation,
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
