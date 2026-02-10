package ch.clicktotranslate.segment.application;

import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;

public interface EventPublisher {

	void publish(SegmentBundleCreatedEvent event);

}
