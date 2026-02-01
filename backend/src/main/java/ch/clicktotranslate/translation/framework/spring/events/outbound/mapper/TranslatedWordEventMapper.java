package ch.clicktotranslate.translation.framework.spring.events.outbound.mapper;

import ch.clicktotranslate.translation.domain.event.TranslatedWordEvent;
import ch.clicktotranslate.translation.framework.spring.events.outbound.dto.TranslatedWordEventDto;

public class TranslatedWordEventMapper {
	public TranslatedWordEventDto map(TranslatedWordEvent event) {
		return new TranslatedWordEventDto();
	}
}
