package ch.clicktotranslate.segment.application;

public interface EventPublisher {

	void publish(TranslatedWordEvent event);

}
