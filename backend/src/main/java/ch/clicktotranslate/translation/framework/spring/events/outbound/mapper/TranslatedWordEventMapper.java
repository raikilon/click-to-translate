package ch.clicktotranslate.translation.framework.spring.events.outbound.mapper;

import ch.clicktotranslate.translation.framework.spring.events.outbound.dto.TranslatedWordEventDto;
import ch.clicktotranslate.translation.infrastructure.event.TranslatedWordEvent;

public class TranslatedWordEventMapper {
	public TranslatedWordEventDto map(TranslatedWordEvent event) {
		return new TranslatedWordEventDto(
				event.userId(),
				event.word(),
				event.sentence(),
				event.wordTranslation(),
				event.sentenceTranslation(),
				event.sourceLanguage(),
				event.targetLanguage(),
				mapSource(event.source()),
				mapSourceMetadata(event.sourceMetadata()),
				event.occurredAt()
		);
	}

	private TranslatedWordEventDto.SourceDto mapSource(TranslatedWordEvent.Source source) {
		if (source == null) {
			return null;
		}

		return new TranslatedWordEventDto.SourceDto(
				source.type(),
				source.id(),
				source.title()
		);
	}

	private TranslatedWordEventDto.SourceMetadataDto mapSourceMetadata(TranslatedWordEvent.SourceMetadata sourceMetadata) {
        switch (sourceMetadata) {
            case TranslatedWordEvent.GenericSourceMetadata generic -> {
                return new TranslatedWordEventDto.GenericSourceMetadataDto(
						generic.url(),
						generic.domain(),
						generic.selectionOffset(),
						generic.paragraphIndex()
				);
            }
            case TranslatedWordEvent.YoutubeSourceMetadata youtube -> {
                return new TranslatedWordEventDto.YoutubeSourceMetadataDto(
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

