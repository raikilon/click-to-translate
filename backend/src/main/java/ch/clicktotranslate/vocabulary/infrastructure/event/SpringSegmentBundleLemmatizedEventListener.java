package ch.clicktotranslate.vocabulary.infrastructure.event;

import ch.clicktotranslate.lemmatizer.domain.SegmentBundleLemmatizedEvent;
import ch.clicktotranslate.vocabulary.domain.SegmentBundle;
import ch.clicktotranslate.vocabulary.application.RegisterSegmentBundle;
import org.jmolecules.event.annotation.DomainEventHandler;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.ApplicationModuleListener;

public class SpringSegmentBundleLemmatizedEventListener {

	private final SegmentBundleLemmatizedEventMapper eventMapper;

	private final RegisterSegmentBundle registerSegmentBundle;

	public SpringSegmentBundleLemmatizedEventListener(SegmentBundleLemmatizedEventMapper eventMapper,
			RegisterSegmentBundle registerSegmentBundle) {
		this.eventMapper = eventMapper;
		this.registerSegmentBundle = registerSegmentBundle;
	}

	@ApplicationModuleListener
	@DomainEventHandler
	public void onTranslatedWord(SegmentBundleLemmatizedEvent segmentBundleLemmatizedEvent) {
		SegmentBundle event = eventMapper.map(segmentBundleLemmatizedEvent);
		registerSegmentBundle.execute(event);
	}

}
