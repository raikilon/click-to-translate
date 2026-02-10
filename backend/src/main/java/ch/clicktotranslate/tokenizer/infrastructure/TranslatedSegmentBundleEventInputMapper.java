package ch.clicktotranslate.tokenizer.infrastructure;

import ch.clicktotranslate.tokenizer.application.TokenizeTranslatedSegmentBundle;
import ch.clicktotranslate.segment.infrastructure.event.TranslatedSegmentBundleEventDto;

public class TranslatedSegmentBundleEventInputMapper {

	public TokenizeTranslatedSegmentBundle map(TranslatedSegmentBundleEventDto eventDto) {
		return new TokenizeTranslatedSegmentBundle(eventDto.userId(), eventDto.word(), eventDto.sentence(),
				eventDto.wordTranslation(), eventDto.sentenceTranslation(), eventDto.sourceLanguage(),
				eventDto.targetLanguage(), eventDto.occurredAt());
	}

}
