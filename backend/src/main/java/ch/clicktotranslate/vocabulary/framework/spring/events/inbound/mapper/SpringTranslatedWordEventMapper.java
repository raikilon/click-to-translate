package ch.clicktotranslate.vocabulary.framework.spring.events.inbound.mapper;

import ch.clicktotranslate.translation.framework.spring.events.outbound.dto.TranslatedWordEventDto;
import ch.clicktotranslate.vocabulary.domain.usecase.input.RegisterUsageInput;

public class SpringTranslatedWordEventMapper {
	public RegisterUsageInput map(TranslatedWordEventDto event) {
		return new RegisterUsageInput();
	}
}
