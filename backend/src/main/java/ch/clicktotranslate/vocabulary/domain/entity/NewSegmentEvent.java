package ch.clicktotranslate.vocabulary.domain.entity;

import org.jmolecules.event.annotation.DomainEvent;
import ch.clicktotranslate.vocabulary.domain.Language;

@DomainEvent
public record NewSegmentEvent(String userId, String tokenized, String word, String sentence, String tokenizedTranlation,
		String wordTranslation, String sentenceTranslation, Language sourceLanguage, Language targetLanguage) {
}
