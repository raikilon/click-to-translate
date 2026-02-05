package ch.clicktotranslate.vocabulary.framework.spring.events.outbound.mapper;

import ch.clicktotranslate.vocabulary.domain.event.WordCreatedEvent;
import ch.clicktotranslate.vocabulary.framework.spring.events.outbound.dto.WordCreatedEventDto;

public class WordCreatedEventMapper {

	public WordCreatedEventDto map(WordCreatedEvent event) {
		return new WordCreatedEventDto();
	}

}
