package ch.clicktotranslate.vocabulary.domain.entity;

import org.jmolecules.event.annotation.DomainEvent;
import ch.clicktotranslate.vocabulary.domain.Language;

@DomainEvent
public record NewSegmentEvent(String userId, String word, String sentence, String wordTranslation,
		String sentenceTranslation, Language sourceLanguage, Language targetLanguage) {
}
