package ch.clicktotranslate.vocabulary.domain;

public record SegmentBundle(UserId userId, String tokenizedWord, String word, String sentence,
		String tokenizedWordTranslation, String wordTranslation, String sentenceTranslation, Language sourceLanguage,
		Language targetLanguage) {

	public SegmentBundle {
		if (userId == null || isMissing(tokenizedWord) || isMissing(tokenizedWordTranslation) || sourceLanguage == null
				|| targetLanguage == null) {
			throw new IllegalArgumentException("Invalid segment bundle parameters.");
		}
	}

	public SegmentBundle(UserId userId, String tokenized, String word, String sentence, String tokenizedTranslation,
			String wordTranslation, String sentenceTranslation, String sourceLanguage, String targetLanguage) {
		this(userId, tokenized, word, sentence, tokenizedTranslation, wordTranslation, sentenceTranslation,
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
