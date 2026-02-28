package ch.clicktotranslate.segment.infrastructure;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record SegmentBundleDto(String word, String sentence, LanguageDto sourceLanguage,
		@NotNull LanguageDto targetLanguage, Instant occurredAt) {
}
