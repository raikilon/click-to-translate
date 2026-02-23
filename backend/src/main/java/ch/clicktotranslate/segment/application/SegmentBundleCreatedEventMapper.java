package ch.clicktotranslate.segment.application;

import ch.clicktotranslate.segment.domain.Segment;
import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;

public class SegmentBundleCreatedEventMapper {

	public SegmentBundleCreatedEvent map(SegmentBundle request, Segment translatedSegment) {
		return new SegmentBundleCreatedEvent("test", request.word(), request.sentence(),
				translatedSegment.translatedWord(), translatedSegment.translatedSentence(), request.sourceLanguage(),
				request.targetLanguage(), mapSource(request.source()), mapSourceMetadata(request.sourceMetadata()),
				request.occurredAt());
	}

	private SegmentBundleCreatedEvent.Source mapSource(SegmentBundle.Source source) {
		if (source == null) {
			return null;
		}

		return new SegmentBundleCreatedEvent.Source(source.type(), source.id(), source.title());
	}

	private SegmentBundleCreatedEvent.SourceMetadata mapSourceMetadata(SegmentBundle.SourceMetadata sourceMetadata) {
		switch (sourceMetadata) {
			case SegmentBundle.GenericSourceMetadata generic -> {
				return new SegmentBundleCreatedEvent.GenericSourceMetadata(generic.url(), generic.domain(),
						generic.selectionOffset(), generic.paragraphIndex());
			}
			case SegmentBundle.YoutubeSourceMetadata youtube -> {
				return new SegmentBundleCreatedEvent.YoutubeSourceMetadata(youtube.url(), youtube.domain(),
						youtube.videoId(), youtube.timestampSeconds());
			}
			default -> {
				return null;
			}
		}
	}

}
