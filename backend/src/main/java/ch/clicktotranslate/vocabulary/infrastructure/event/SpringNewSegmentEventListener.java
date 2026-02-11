package ch.clicktotranslate.vocabulary.infrastructure.event;

import ch.clicktotranslate.tokenizer.domain.SegmentBundleTokenizedEvent;
import ch.clicktotranslate.vocabulary.domain.entity.NewSegmentEvent;
import ch.clicktotranslate.vocabulary.domain.RegisterUsageFromTranslation;
import org.jmolecules.event.annotation.DomainEventHandler;
import org.springframework.context.event.EventListener;

public class SpringNewSegmentEventListener {

	private final SpringNewSegmentEventMapper eventMapper;

	private final RegisterUsageFromTranslation registerUsageFromTranslation;

	public SpringNewSegmentEventListener(SpringNewSegmentEventMapper eventMapper,
			RegisterUsageFromTranslation registerUsageFromTranslation) {
		this.eventMapper = eventMapper;
		this.registerUsageFromTranslation = registerUsageFromTranslation;
	}

	@EventListener
	@DomainEventHandler
	public void onTranslatedWord(SegmentBundleTokenizedEvent eventDto) {
		NewSegmentEvent event = eventMapper.map(eventDto);
		registerUsageFromTranslation.execute(event);
	}

}
