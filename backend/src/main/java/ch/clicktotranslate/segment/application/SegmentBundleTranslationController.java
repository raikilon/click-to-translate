package ch.clicktotranslate.segment.application;

import ch.clicktotranslate.segment.domain.Segment;
import ch.clicktotranslate.segment.domain.SegmentTranslation;
import ch.clicktotranslate.segment.domain.TranslatedSegment;

public class SegmentBundleTranslationController {

	private final SegmentTranslation segmentTranslation;

	private final EventPublisher eventPublisher;

	private final TranslatedWordEventMapper translatedWordEventMapper;

	private final SegmentBundleMapper segmentBundleMapper;

	public SegmentBundleTranslationController(SegmentTranslation segmentTranslation, EventPublisher eventPublisher,
			TranslatedWordEventMapper translatedWordEventMapper, SegmentBundleMapper segmentBundleMapper) {
		this.segmentTranslation = segmentTranslation;
		this.eventPublisher = eventPublisher;
		this.translatedWordEventMapper = translatedWordEventMapper;
		this.segmentBundleMapper = segmentBundleMapper;
	}

	public TranslatedSegment translate(SegmentBundle segmentBundle) {
		Segment segment = this.segmentBundleMapper.map(segmentBundle);

		TranslatedSegment translatedSegment = segmentTranslation.translate(segment);

		TranslatedWordEvent event = translatedWordEventMapper.map(segmentBundle, translatedSegment);

		eventPublisher.publish(event);

		return translatedSegment;
	}

}
