package ch.clicktotranslate.tokenizer.infrastructure;

import java.time.Instant;

import org.springframework.modulith.NamedInterface;

@NamedInterface("event")
public record TokenizedSegmentBundleEventDto(String userId, String word, String sentence, String wordTranslation,
		String sentenceTranslation, String sourceLanguage, String targetLanguage, Instant occurredAt) {
}
