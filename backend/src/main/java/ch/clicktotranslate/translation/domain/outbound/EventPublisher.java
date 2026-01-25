package ch.clicktotranslate.translation.domain.outbound;

import ch.clicktotranslate.translation.domain.event.TranslatedWordDomainEvent;

public interface EventPublisher {
	void publish(TranslatedWordDomainEvent event);
}
