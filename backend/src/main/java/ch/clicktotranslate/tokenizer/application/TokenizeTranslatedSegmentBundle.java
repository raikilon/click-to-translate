package ch.clicktotranslate.tokenizer.application;

import ch.clicktotranslate.tokenizer.domain.Tokenizer;

public class TokenizeTranslatedSegmentBundle {

	private final Tokenizer tokenizer;

	private final EventPublisher eventPublisher;

	public TokenizeTranslatedSegmentBundle(Tokenizer tokenizer, EventPublisher eventPublisher) {
		this.tokenizer = tokenizer;
		this.eventPublisher = eventPublisher;
	}

	public void execute(TokenizeTranslatedSegmentBundleInput input) {
		String tokenizedWord = tokenizer.tokenize(input.word());
		TokenizedSegmentBundleEvent event = new TokenizedSegmentBundleEvent(input.userId(), tokenizedWord,
				input.sentence(), input.wordTranslation(), input.sentenceTranslation(), input.sourceLanguage(),
				input.targetLanguage(), input.occurredAt());
		eventPublisher.publish(event);
	}

}
