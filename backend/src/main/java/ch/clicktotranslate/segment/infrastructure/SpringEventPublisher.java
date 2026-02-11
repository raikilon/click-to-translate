package ch.clicktotranslate.segment.infrastructure;

import ch.clicktotranslate.segment.application.TextTranslator;
import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;
import org.jmolecules.event.annotation.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

import ch.clicktotranslate.segment.application.EventPublisher;

public class SpringEventPublisher implements EventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	@DomainEventPublisher
	public void publish(SegmentBundleCreatedEvent event) {
		applicationEventPublisher.publishEvent(event);
	}

}
