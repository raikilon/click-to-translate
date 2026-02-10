package ch.clicktotranslate.tokenizer.application;

import java.time.Instant;

public record TokenizeTranslatedSegmentBundleInput(String userId, String word, String sentence, String wordTranslation,
		String sentenceTranslation, String sourceLanguage, String targetLanguage, Instant occurredAt) {
}
