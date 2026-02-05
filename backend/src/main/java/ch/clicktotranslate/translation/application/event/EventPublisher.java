package ch.clicktotranslate.translation.application.event;

public interface EventPublisher {

	void publish(TranslatedWordEvent event);

}
