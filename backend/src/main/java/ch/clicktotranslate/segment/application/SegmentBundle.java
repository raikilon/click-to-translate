package ch.clicktotranslate.segment.application;

import java.time.Instant;

public record SegmentBundle(String word, String sentence, String sourceLanguage, String targetLanguage,
		Instant occurredAt) {
}
