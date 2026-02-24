package ch.clicktotranslate.vocabulary.infrastructure.event;

import ch.clicktotranslate.auth.UserId;
import ch.clicktotranslate.lemmatizer.domain.SegmentBundleLemmatizedEvent;
import ch.clicktotranslate.vocabulary.domain.SegmentBundle;

public class SegmentBundleLemmatizedEventMapper {

	public SegmentBundle map(SegmentBundleLemmatizedEvent event) {
		return new SegmentBundle(UserId.of(event.userId()), event.lemmatizedWord(), event.word(), event.sentence(),
				event.lemmatizedWordTranslation(), event.wordTranslation(), event.sentenceTranslation(),
				event.sourceLanguage(), event.targetLanguage());
	}

}
