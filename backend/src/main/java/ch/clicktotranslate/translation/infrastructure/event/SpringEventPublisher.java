package ch.clicktotranslate.translation.infrastructure.event;

import org.jmolecules.event.annotation.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

import ch.clicktotranslate.translation.application.event.TranslatedWordEvent;
import ch.clicktotranslate.translation.application.event.EventPublisher;

public class SpringEventPublisher implements EventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	private final TranslatedSegmentEventMapper eventMapper;

	public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher,
			TranslatedSegmentEventMapper eventMapper) {
		this.applicationEventPublisher = applicationEventPublisher;
		this.eventMapper = eventMapper;
	}

	@Override
	@DomainEventPublisher
	public void publish(TranslatedWordEvent event) {
		TranslatedSegmentEventDto springEvent = eventMapper.map(event);
		applicationEventPublisher.publishEvent(springEvent);
	}

}
