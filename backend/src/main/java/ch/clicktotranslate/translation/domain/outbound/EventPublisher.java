package ch.clicktotranslate.translation.domain.outbound;

import ch.clicktotranslate.translation.domain.event.TranslatedWordEvent;

public interface EventPublisher {
	void publish(TranslatedWordEvent event);
}
