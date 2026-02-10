package ch.clicktotranslate.tokenizer.application;

import ch.clicktotranslate.tokenizer.domain.SegmentBundleTokenizedEvent;

public interface SegmentBundleTokenizedEventPublisher {

	void publish(SegmentBundleTokenizedEvent event);

}
