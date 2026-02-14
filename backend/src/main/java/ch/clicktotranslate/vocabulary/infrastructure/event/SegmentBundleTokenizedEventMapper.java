package ch.clicktotranslate.vocabulary.infrastructure.event;

import ch.clicktotranslate.tokenizer.domain.SegmentBundleTokenizedEvent;
import ch.clicktotranslate.vocabulary.domain.entity.SegmentBundle;

public class SegmentBundleTokenizedEventMapper {

	public SegmentBundle map(SegmentBundleTokenizedEvent event) {
		return new SegmentBundle(event.userId(), event.tokenizedWord(), event.word(), event.sentence(),
				event.tokenizedWordTranslation(), event.wordTranslation(), event.sentenceTranslation(),
				event.sourceLanguage(), event.targetLanguage());
	}

}
