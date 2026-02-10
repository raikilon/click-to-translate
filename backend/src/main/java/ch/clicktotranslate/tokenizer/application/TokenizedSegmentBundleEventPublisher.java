package ch.clicktotranslate.tokenizer.application;

public interface TokenizedSegmentBundleEventPublisher {

	void publish(TokenizedSegmentBundleEvent event);

}
