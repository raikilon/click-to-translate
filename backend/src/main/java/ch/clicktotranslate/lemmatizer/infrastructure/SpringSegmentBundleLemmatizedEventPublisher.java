package ch.clicktotranslate.lemmatizer.infrastructure;

import org.jmolecules.event.annotation.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

import ch.clicktotranslate.lemmatizer.application.SegmentBundleLemmatizedEventPublisher;
import ch.clicktotranslate.lemmatizer.domain.SegmentBundleLemmatizedEvent;

public class SpringSegmentBundleLemmatizedEventPublisher implements SegmentBundleLemmatizedEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	public SpringSegmentBundleLemmatizedEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	@DomainEventPublisher
	public void publish(SegmentBundleLemmatizedEvent event) {
		applicationEventPublisher.publishEvent(event);
	}

}
