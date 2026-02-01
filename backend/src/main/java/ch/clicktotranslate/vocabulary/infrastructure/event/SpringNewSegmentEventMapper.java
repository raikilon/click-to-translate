package ch.clicktotranslate.vocabulary.infrastructure.event;

import ch.clicktotranslate.translation.infrastructure.event.TranslatedSegmentBundleEventDto;
import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.entity.NewSegmentEvent;

public class SpringNewSegmentEventMapper {

	public NewSegmentEvent map(TranslatedSegmentBundleEventDto event) {
		return new NewSegmentEvent(event.userId(), event.word(), event.sentence(), event.wordTranslation(),
				event.sentenceTranslation(), toLanguage(event.sourceLanguage()), toLanguage(event.targetLanguage()));
	}

	private Language toLanguage(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return Language.valueOf(value);
	}

}
