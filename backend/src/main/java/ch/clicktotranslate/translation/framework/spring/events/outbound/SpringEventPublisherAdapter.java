package ch.clicktotranslate.translation.framework.spring.events.outbound;

import org.springframework.context.ApplicationEventPublisher;

import ch.clicktotranslate.translation.domain.event.TranslatedWordDomainEvent;
import ch.clicktotranslate.translation.domain.outbound.EventPublisher;

public class SpringEventPublisherAdapter implements EventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	public SpringEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	public void publish(TranslatedWordDomainEvent event) {
		applicationEventPublisher.publishEvent(event);
	}
}
