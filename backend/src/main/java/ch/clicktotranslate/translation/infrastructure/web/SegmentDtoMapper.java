package ch.clicktotranslate.translation.infrastructure.web;

import ch.clicktotranslate.translation.application.web.SegmentBundle;

public class SegmentDtoMapper {

	public TranslatedSegmentDto toDto(ch.clicktotranslate.translation.domain.TranslatedSegment response) {
		return new TranslatedSegmentDto(response.word(), response.sentence(), response.translatedWord(),
				response.translatedSentence());
	}

	public SegmentBundle toDomain(SegmentBundleDto request) {
		return new SegmentBundle(request.userId(), request.word(), request.sentence(), request.sourceLanguage(),
				request.targetLanguage(), mapSource(request.source()), mapSourceMetadata(request.sourceMetadata()),
				request.occurredAt());
	}

	private SegmentBundle.Source mapSource(SegmentBundleDto.SourceDto source) {
		if (source == null) {
			return null;
		}
		return new SegmentBundle.Source(source.type(), source.id(), source.title());
	}

	private SegmentBundle.SourceMetadata mapSourceMetadata(SegmentBundleDto.SourceMetadataDto sourceMetadata) {
		if (sourceMetadata == null) {
			return null;
		}

		return switch (sourceMetadata) {
			case SegmentBundleDto.GenericSourceMetadataDto generic -> new SegmentBundle.GenericSourceMetadata(
					generic.url(), generic.domain(), generic.selectionOffset(), generic.paragraphIndex());
			case SegmentBundleDto.YoutubeSourceMetadataDto youtube -> new SegmentBundle.YoutubeSourceMetadata(
					youtube.url(), youtube.domain(), youtube.videoId(), youtube.timestampSeconds());
			default -> null;
		};
	}

}
