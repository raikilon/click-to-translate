package ch.clicktotranslate.lemmatizer.application;

import ch.clicktotranslate.lemmatizer.domain.SegmentBundleLemmatizedEvent;

public interface SegmentBundleLemmatizedEventPublisher {

	void publish(SegmentBundleLemmatizedEvent event);

}
