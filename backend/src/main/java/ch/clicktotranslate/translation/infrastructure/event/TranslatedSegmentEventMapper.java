package ch.clicktotranslate.translation.infrastructure.event;

import ch.clicktotranslate.translation.application.event.TranslatedWordEvent;

public class TranslatedSegmentEventMapper {

	public TranslatedSegmentEventDto map(TranslatedWordEvent event) {
		return new TranslatedSegmentEventDto(event.userId(), event.word(), event.sentence(), event.wordTranslation(),
				event.sentenceTranslation(), event.sourceLanguage().code(), event.targetLanguage().code(),
				mapSource(event.source()), mapSourceMetadata(event.sourceMetadata()), event.occurredAt());
	}

	private TranslatedSegmentEventDto.SourceDto mapSource(TranslatedWordEvent.Source source) {
		if (source == null) {
			return null;
		}

		return new TranslatedSegmentEventDto.SourceDto(source.type(), source.id(), source.title());
	}

	private TranslatedSegmentEventDto.SourceMetadataDto mapSourceMetadata(
			TranslatedWordEvent.SourceMetadata sourceMetadata) {
		switch (sourceMetadata) {
			case TranslatedWordEvent.GenericSourceMetadata generic -> {
				return new TranslatedSegmentEventDto.GenericSourceMetadataDto(generic.url(), generic.domain(),
						generic.selectionOffset(), generic.paragraphIndex());
			}
			case TranslatedWordEvent.YoutubeSourceMetadata youtube -> {
				return new TranslatedSegmentEventDto.YoutubeSourceMetadataDto(youtube.url(), youtube.domain(),
						youtube.videoId(), youtube.timestampSeconds());
			}
			default -> {
				return null;
			}
		}
	}

}
