package ch.clicktotranslate.lemmatizer.application;

import java.time.Instant;

public record TranslatedSegmentBundle(String userId, String word, String sentence, String wordTranslation,
		String sentenceTranslation, String sourceLanguage, String targetLanguage, Instant occurredAt) {
}
