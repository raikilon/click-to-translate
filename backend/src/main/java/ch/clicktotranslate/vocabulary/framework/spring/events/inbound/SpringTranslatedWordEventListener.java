package ch.clicktotranslate.vocabulary.framework.spring.events.inbound;

import org.springframework.context.event.EventListener;

import ch.clicktotranslate.translation.infrastructure.event.TranslatedSegmentEventDto;
import ch.clicktotranslate.vocabulary.domain.usecase.input.RegisterUsageInput;
import ch.clicktotranslate.vocabulary.framework.spring.events.inbound.mapper.SpringTranslatedWordEventMapper;
import ch.clicktotranslate.vocabulary.infrastructure.event.TranslatedWordEventHandler;

public class SpringTranslatedWordEventListener {

	private final SpringTranslatedWordEventMapper eventMapper;

	private final TranslatedWordEventHandler eventHandler;

	public SpringTranslatedWordEventListener(SpringTranslatedWordEventMapper eventMapper,
			TranslatedWordEventHandler eventHandler) {
		this.eventMapper = eventMapper;
		this.eventHandler = eventHandler;
	}

	@EventListener
	public void onTranslatedWord(TranslatedSegmentEventDto event) {
		RegisterUsageInput input = eventMapper.map(event);
		eventHandler.handle(input);
	}

}
