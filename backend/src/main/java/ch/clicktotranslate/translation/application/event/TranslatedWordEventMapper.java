package ch.clicktotranslate.translation.application.event;

import ch.clicktotranslate.translation.domain.TranslatedSegment;
import ch.clicktotranslate.translation.application.web.SegmentBundle;

public class TranslatedWordEventMapper {

	public TranslatedWordEvent map(SegmentBundle request, TranslatedSegment translatedSegment) {
		return new TranslatedWordEvent(request.userId(), request.word(), request.sentence(),
				translatedSegment.translatedWord(), translatedSegment.translatedSentence(), request.sourceLanguage(),
				request.targetLanguage(), mapSource(request.source()), mapSourceMetadata(request.sourceMetadata()),
				request.occurredAt());
	}

	private TranslatedWordEvent.Source mapSource(SegmentBundle.Source source) {
		if (source == null) {
			return null;
		}

		return new TranslatedWordEvent.Source(source.type(), source.id(), source.title());
	}

	private TranslatedWordEvent.SourceMetadata mapSourceMetadata(SegmentBundle.SourceMetadata sourceMetadata) {
		switch (sourceMetadata) {
			case SegmentBundle.GenericSourceMetadata generic -> {
				return new TranslatedWordEvent.GenericSourceMetadata(generic.url(), generic.domain(),
						generic.selectionOffset(), generic.paragraphIndex());
			}
			case SegmentBundle.YoutubeSourceMetadata youtube -> {
				return new TranslatedWordEvent.YoutubeSourceMetadata(youtube.url(), youtube.domain(), youtube.videoId(),
						youtube.timestampSeconds());
			}
			default -> {
				return null;
			}
		}
	}

}
