package ch.clicktotranslate.tokenizer.infrastructure;

import org.jmolecules.event.annotation.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

import ch.clicktotranslate.tokenizer.application.TokenizedSegmentBundleEventPublisher;
import ch.clicktotranslate.tokenizer.application.TokenizedSegmentBundleEvent;

public class SpringTokenizedSegmentBundleEventPublisher implements TokenizedSegmentBundleEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	private final TokenizedSegmentBundleEventMapper eventMapper;

	public SpringTokenizedSegmentBundleEventPublisher(ApplicationEventPublisher applicationEventPublisher,
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
