package ch.clicktotranslate.tokenizer.application;

import java.time.Instant;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record TokenizedSegmentBundleEvent(String userId, String word, String sentence, String wordTranslation,
		String sentenceTranslation, String sourceLanguage, String targetLanguage, Instant occurredAt) {
}
