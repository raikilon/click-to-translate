package ch.clicktotranslate.translation.infrastructure.event;

public interface EventPublisher {
	void publish(TranslatedWordEvent event);
}
