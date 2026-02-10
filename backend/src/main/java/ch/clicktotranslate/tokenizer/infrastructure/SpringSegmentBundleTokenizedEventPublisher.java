package ch.clicktotranslate.tokenizer.infrastructure;

import org.jmolecules.event.annotation.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

import ch.clicktotranslate.tokenizer.application.SegmentBundleTokenizedEventPublisher;
import ch.clicktotranslate.tokenizer.domain.SegmentBundleTokenizedEvent;

public class SpringSegmentBundleTokenizedEventPublisher implements SegmentBundleTokenizedEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	public SpringSegmentBundleTokenizedEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	@DomainEventPublisher
	public void publish(SegmentBundleTokenizedEvent event) {
		applicationEventPublisher.publishEvent(event);
	}

}
