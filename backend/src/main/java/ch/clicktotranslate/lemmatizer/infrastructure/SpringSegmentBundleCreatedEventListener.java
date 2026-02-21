package ch.clicktotranslate.lemmatizer.infrastructure;

import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;
import org.jmolecules.event.annotation.DomainEventHandler;
import org.springframework.context.event.EventListener;

import ch.clicktotranslate.lemmatizer.application.SegmentBundleCreatedController;
import ch.clicktotranslate.lemmatizer.application.TranslatedSegmentBundle;

public class SpringSegmentBundleCreatedEventListener {

	private final SegmentBundleCreatedController segmentBundleCreatedController;

	private final SegmentBundleTranslatedEventMapper eventMapper;

	public SpringSegmentBundleCreatedEventListener(SegmentBundleCreatedController segmentBundleCreatedController,
			SegmentBundleTranslatedEventMapper eventMapper) {
		this.segmentBundleCreatedController = segmentBundleCreatedController;
		this.eventMapper = eventMapper;
	}

	@EventListener
	@DomainEventHandler
	public void onTranslatedSegmentBundle(SegmentBundleCreatedEvent event) {
		TranslatedSegmentBundle segmentBundle = eventMapper.map(event);
		segmentBundleCreatedController.lemmatize(segmentBundle);
	}

}
