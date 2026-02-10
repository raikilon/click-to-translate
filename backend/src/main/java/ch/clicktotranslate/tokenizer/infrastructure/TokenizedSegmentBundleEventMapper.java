package ch.clicktotranslate.tokenizer.infrastructure;

import ch.clicktotranslate.tokenizer.application.TokenizedSegmentBundleEvent;

public class TokenizedSegmentBundleEventMapper {

	public TokenizedSegmentBundleEventDto map(TokenizedSegmentBundleEvent event) {
		return new TokenizedSegmentBundleEventDto(event.userId(), event.tokenizedWord(),
				event.tokenizedWordTranslation(), event.sentence(), event.sentenceTranslation(), event.word(),
				event.wordTranslation(), event.sourceLanguage(), event.targetLanguage(), event.occurredAt());
	}

}
