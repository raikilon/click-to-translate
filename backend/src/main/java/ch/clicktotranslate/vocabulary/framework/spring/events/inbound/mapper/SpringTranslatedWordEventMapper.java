package ch.clicktotranslate.vocabulary.framework.spring.events.inbound.mapper;

import ch.clicktotranslate.translation.domain.event.TranslatedWordDomainEvent;
import ch.clicktotranslate.vocabulary.domain.usecase.model.RegisterUsageInput;

public class SpringTranslatedWordEventMapper {
	public RegisterUsageInput map(TranslatedWordDomainEvent event) {
		return new RegisterUsageInput();
	}
}
