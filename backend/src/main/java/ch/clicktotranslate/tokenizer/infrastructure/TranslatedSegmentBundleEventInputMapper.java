package ch.clicktotranslate.tokenizer.infrastructure;

import ch.clicktotranslate.tokenizer.application.TokenizeTranslatedSegmentBundleInput;
import ch.clicktotranslate.segment.infrastructure.event.TranslatedSegmentBundleEventDto;

public class TranslatedSegmentBundleEventInputMapper {

	public TokenizeTranslatedSegmentBundleInput map(TranslatedSegmentBundleEventDto eventDto) {
		return new TokenizeTranslatedSegmentBundleInput(eventDto.userId(), eventDto.word(), eventDto.sentence(),
				eventDto.wordTranslation(), eventDto.sentenceTranslation(), eventDto.sourceLanguage(),
				eventDto.targetLanguage(), eventDto.occurredAt());
	}

}
