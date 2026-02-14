package ch.clicktotranslate.vocabulary.infrastructure.event;

import ch.clicktotranslate.tokenizer.domain.SegmentBundleTokenizedEvent;
import ch.clicktotranslate.vocabulary.domain.SegmentBundle;
import ch.clicktotranslate.vocabulary.application.RegisterSegmentBundle;
import org.jmolecules.event.annotation.DomainEventHandler;
import org.springframework.context.event.EventListener;

public class SpringSegmentBundleTokenizedEventListener {

	private final SegmentBundleTokenizedEventMapper eventMapper;

	private final RegisterSegmentBundle registerSegmentBundle;

	public SpringSegmentBundleTokenizedEventListener(SegmentBundleTokenizedEventMapper eventMapper,
			RegisterSegmentBundle registerSegmentBundle) {
		this.eventMapper = eventMapper;
		this.registerSegmentBundle = registerSegmentBundle;
	}

	@EventListener
	@DomainEventHandler
	public void onTranslatedWord(SegmentBundleTokenizedEvent segmentBundleTokenizedEvent) {
		SegmentBundle event = eventMapper.map(segmentBundleTokenizedEvent);
		registerSegmentBundle.execute(event);
	}

}
