package ch.clicktotranslate.tokenizer.infrastructure;

import ch.clicktotranslate.tokenizer.application.TokenizedSegmentBundleEvent;

public class TokenizedSegmentBundleEventMapper {

	public TokenizedSegmentBundleEventDto map(TokenizedSegmentBundleEvent event) {
		return new TokenizedSegmentBundleEventDto(event.userId(), event.word(), event.sentence(),
				event.wordTranslation(), event.sentenceTranslation(), event.sourceLanguage(), event.targetLanguage(),
				event.occurredAt());
	}

}
