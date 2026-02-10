package ch.clicktotranslate.tokenizer.infrastructure;

import java.time.Instant;

import org.jmolecules.event.annotation.DomainEvent;
import org.springframework.modulith.NamedInterface;

@DomainEvent
@NamedInterface("event")
public record TokenizedSegmentBundleEventDto(String userId, String tokenizedWord, String tokenizedWordTranslation,
		String sentence, String sentenceTranslation, String word, String wordTranslation, String sourceLanguage,
		String targetLanguage, Instant occurredAt) {
}
