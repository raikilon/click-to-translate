package ch.clicktotranslate.tokenizer.application;

public interface EventPublisher {

	void publish(TokenizedSegmentBundleEvent event);

}
