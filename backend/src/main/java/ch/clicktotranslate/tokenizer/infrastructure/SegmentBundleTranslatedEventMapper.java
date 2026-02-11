package ch.clicktotranslate.tokenizer.infrastructure;

import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;
import ch.clicktotranslate.tokenizer.application.TranslatedSegmentBundle;

public class SegmentBundleTranslatedEventMapper {

	public TranslatedSegmentBundle map(SegmentBundleCreatedEvent eventDto) {
		return new TranslatedSegmentBundle(eventDto.userId(), eventDto.word(), eventDto.sentence(),
				eventDto.wordTranslation(), eventDto.sentenceTranslation(), eventDto.sourceLanguage(),
				eventDto.targetLanguage(), eventDto.occurredAt());
	}

}
