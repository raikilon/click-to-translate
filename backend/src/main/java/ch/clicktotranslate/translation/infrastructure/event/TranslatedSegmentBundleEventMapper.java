package ch.clicktotranslate.translation.infrastructure.event;

import ch.clicktotranslate.translation.application.event.TranslatedWordEvent;

public class TranslatedSegmentBundleEventMapper {

	public TranslatedSegmentBundleEventDto map(TranslatedWordEvent event) {
		return new TranslatedSegmentBundleEventDto(event.userId(), event.word(), event.sentence(),
				event.wordTranslation(), event.sentenceTranslation(), event.sourceLanguage().name(),
				event.targetLanguage().name(), mapSource(event.source()), mapSourceMetadata(event.sourceMetadata()),
				event.occurredAt());
	}

	private TranslatedSegmentBundleEventDto.SourceDto mapSource(TranslatedWordEvent.Source source) {
		if (source == null) {
			return null;
		}

		return new TranslatedSegmentBundleEventDto.SourceDto(source.type(), source.id(), source.title());
	}

	private TranslatedSegmentBundleEventDto.SourceMetadataDto mapSourceMetadata(
			TranslatedWordEvent.SourceMetadata sourceMetadata) {
		switch (sourceMetadata) {
			case TranslatedWordEvent.GenericSourceMetadata generic -> {
				return new TranslatedSegmentBundleEventDto.GenericSourceMetadataDto(generic.url(), generic.domain(),
						generic.selectionOffset(), generic.paragraphIndex());
			}
			case TranslatedWordEvent.YoutubeSourceMetadata youtube -> {
				return new TranslatedSegmentBundleEventDto.YoutubeSourceMetadataDto(youtube.url(), youtube.domain(),
						youtube.videoId(), youtube.timestampSeconds());
			}
			default -> {
				return null;
			}
		}
	}

}
