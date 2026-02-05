package ch.clicktotranslate.vocabulary.framework.spring.events.inbound.mapper;

import ch.clicktotranslate.translation.infrastructure.event.TranslatedSegmentEventDto;
import ch.clicktotranslate.vocabulary.domain.usecase.input.RegisterUsageInput;

public class SpringTranslatedWordEventMapper {

	public RegisterUsageInput map(TranslatedSegmentEventDto event) {
		return new RegisterUsageInput();
	}

}
