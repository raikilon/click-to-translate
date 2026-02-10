package ch.clicktotranslate.tokenizer.application;

import ch.clicktotranslate.tokenizer.domain.WordTokenizer;
import ch.clicktotranslate.tokenizer.domain.TokenizedWord;

public class TokenizeTranslatedSegmentBundleController {

	private final WordTokenizer wordTokenizer;

	private final TokenizedSegmentBundleEventPublisher tokenizedSegmentBundleEventPublisher;

	public TokenizeTranslatedSegmentBundleController(WordTokenizer wordTokenizer,
													 TokenizedSegmentBundleEventPublisher tokenizedSegmentBundleEventPublisher) {
		this.wordTokenizer = wordTokenizer;
		this.tokenizedSegmentBundleEventPublisher = tokenizedSegmentBundleEventPublisher;
	}

	public void tokenize(TokenizeTranslatedSegmentBundle input) {
		TokenizedWord tokenizedWord = wordTokenizer.tokenize(input.word(), input.sourceLanguage(),
				input.targetLanguage());
		TokenizedSegmentBundleEvent event = new TokenizedSegmentBundleEvent(input.userId(),
				tokenizedWord.tokenizedWord(), tokenizedWord.tokenizedWordTranslation(), input.sentence(),
				input.sentenceTranslation(), input.word(), input.wordTranslation(), input.sourceLanguage(),
				input.targetLanguage(), input.occurredAt());
		tokenizedSegmentBundleEventPublisher.publish(event);
	}

}
