package ch.clicktotranslate.tokenizer.infrastructure;

import org.jmolecules.event.annotation.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

import ch.clicktotranslate.tokenizer.application.EventPublisher;
import ch.clicktotranslate.tokenizer.application.TokenizedSegmentBundleEvent;

public class SpringEventPublisher implements EventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	private final TokenizedSegmentBundleEventMapper eventMapper;

	public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher,
			TokenizedSegmentBundleEventMapper eventMapper) {
		this.applicationEventPublisher = applicationEventPublisher;
		this.eventMapper = eventMapper;
	}

	@Override
	@DomainEventPublisher
	public void publish(TokenizedSegmentBundleEvent event) {
		TokenizedSegmentBundleEventDto eventDto = eventMapper.map(event);
		applicationEventPublisher.publishEvent(eventDto);
	}

}
