package ch.clicktotranslate.translation.infrastructure.event;

import ch.clicktotranslate.translation.domain.entity.TranslatedWord;
import ch.clicktotranslate.translation.infrastructure.controller.model.TranslateRequest;

public class TranslatedWordEventMapper {
	public TranslatedWordEvent map(TranslateRequest request, TranslatedWord translatedWord) {
		return new TranslatedWordEvent(
				request.userId(),
				request.word(),
				request.sentence(),
				translatedWord.wordTranslation(),
				translatedWord.sentenceTranslation(),
				request.sourceLanguage(),
				request.targetLanguage(),
				mapSource(request.source()),
				mapSourceMetadata(request.sourceMetadata()),
				request.occurredAt()
		);
	}

	private TranslatedWordEvent.Source mapSource(TranslateRequest.Source source) {
		if (source == null) {
			return null;
		}

		return new TranslatedWordEvent.Source(
				source.type(),
				source.id(),
				source.title()
		);
	}

	private TranslatedWordEvent.SourceMetadata mapSourceMetadata(TranslateRequest.SourceMetadata sourceMetadata) {
        switch (sourceMetadata) {
            case TranslateRequest.GenericSourceMetadata generic -> {
                return new TranslatedWordEvent.GenericSourceMetadata(
						generic.url(),
						generic.domain(),
						generic.selectionOffset(),
						generic.paragraphIndex()
				);
            }
            case TranslateRequest.YoutubeSourceMetadata youtube -> {
                return new TranslatedWordEvent.YoutubeSourceMetadata(
						youtube.url(),
						youtube.domain(),
						youtube.videoId(),
						youtube.timestampSeconds()
				);
            }
            default -> {
				return null;
            }
        }
	}
}

