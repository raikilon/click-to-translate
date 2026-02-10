package ch.clicktotranslate.tokenizer.application;

import java.time.Instant;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record TokenizedSegmentBundleEvent(String userId, String tokenizedWord, String tokenizedWordTranslation,
		String sentence, String sentenceTranslation, String word, String wordTranslation, String sourceLanguage,
		String targetLanguage, Instant occurredAt) {
}
