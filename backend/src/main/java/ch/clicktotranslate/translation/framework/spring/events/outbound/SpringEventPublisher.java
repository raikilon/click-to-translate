package ch.clicktotranslate.translation.framework.spring.events.outbound;

import org.springframework.context.ApplicationEventPublisher;

import ch.clicktotranslate.translation.infrastructure.event.TranslatedWordEvent;
import ch.clicktotranslate.translation.infrastructure.event.EventPublisher;
import ch.clicktotranslate.translation.framework.spring.events.outbound.dto.TranslatedWordEventDto;
import ch.clicktotranslate.translation.framework.spring.events.outbound.mapper.TranslatedWordEventMapper;

public class SpringEventPublisher implements EventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;
	private final TranslatedWordEventMapper eventMapper;

	public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher,
                                TranslatedWordEventMapper eventMapper) {
		this.applicationEventPublisher = applicationEventPublisher;
		this.eventMapper = eventMapper;
	}

	@Override
	public void publish(TranslatedWordEvent event) {
		TranslatedWordEventDto springEvent = eventMapper.map(event);
		applicationEventPublisher.publishEvent(springEvent);
	}
}
