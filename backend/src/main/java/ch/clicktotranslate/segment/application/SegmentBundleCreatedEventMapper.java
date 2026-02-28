package ch.clicktotranslate.segment.application;

import ch.clicktotranslate.segment.domain.Segment;
import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;

public class SegmentBundleCreatedEventMapper {

	public SegmentBundleCreatedEvent map(String userId, SegmentBundle request, Segment translatedSegment) {
		return new SegmentBundleCreatedEvent(userId, request.word(), request.sentence(),
				translatedSegment.translatedWord(), translatedSegment.translatedSentence(), request.sourceLanguage(),
				request.targetLanguage(), request.occurredAt());
	}

}
