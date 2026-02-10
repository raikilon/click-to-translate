package ch.clicktotranslate.tokenizer.infrastructure;

import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;
import org.jmolecules.event.annotation.DomainEventHandler;
import org.springframework.context.event.EventListener;

import ch.clicktotranslate.tokenizer.application.TokenizeTranslatedSegmentBundleController;
import ch.clicktotranslate.tokenizer.application.TranslatedSegmentBundle;

public class SpringSegmentBundleCreatedEventListener {

	private final TokenizeTranslatedSegmentBundleController tokenizeTranslatedSegmentBundleController;

	private final SegmentBundleTranslatedEventMapper eventMapper;

	public SpringSegmentBundleCreatedEventListener(
			TokenizeTranslatedSegmentBundleController tokenizeTranslatedSegmentBundleController,
			SegmentBundleTranslatedEventMapper eventMapper) {
		this.tokenizeTranslatedSegmentBundleController = tokenizeTranslatedSegmentBundleController;
		this.eventMapper = eventMapper;
	}

	@EventListener
	@DomainEventHandler
	public void onTranslatedSegmentBundle(SegmentBundleCreatedEvent event) {
		TranslatedSegmentBundle segmentBundle = eventMapper.map(event);
		tokenizeTranslatedSegmentBundleController.tokenize(segmentBundle);
	}

}
