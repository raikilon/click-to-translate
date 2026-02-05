package ch.clicktotranslate.vocabulary.framework.spring.events.outbound.mapper;

import ch.clicktotranslate.vocabulary.domain.event.WordDeletedEvent;
import ch.clicktotranslate.vocabulary.framework.spring.events.outbound.dto.WordDeletedEventDto;

public class WordDeletedEventMapper {

	public WordDeletedEventDto map(WordDeletedEvent event) {
		return new WordDeletedEventDto();
	}

}
