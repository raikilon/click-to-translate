package ch.clicktotranslate.segment.application;

import ch.clicktotranslate.segment.domain.Segment;
import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;
import org.jmolecules.ddd.annotation.Service;

@Service
public class SegmentBundleTranslationController {

	private final SegmentTranslatorService segmentTranslatorService;

	private final EventPublisher eventPublisher;

	private final SegmentBundleCreatedEventMapper segmentBundleCreatedEventMapper;

	private final SegmentBundleMapper segmentBundleMapper;

	public SegmentBundleTranslationController(SegmentTranslatorService segmentTranslatorService,
			EventPublisher eventPublisher, SegmentBundleCreatedEventMapper segmentBundleCreatedEventMapper,
			SegmentBundleMapper segmentBundleMapper) {
		this.segmentTranslatorService = segmentTranslatorService;
		this.eventPublisher = eventPublisher;
		this.segmentBundleCreatedEventMapper = segmentBundleCreatedEventMapper;
		this.segmentBundleMapper = segmentBundleMapper;
	}

	public Segment translate(SegmentBundle segmentBundle) {
		Segment segment = this.segmentBundleMapper.map(segmentBundle);

		Segment translatedSegment = segmentTranslatorService.translate(segment);

		SegmentBundleCreatedEvent event = segmentBundleCreatedEventMapper.map(segmentBundle, translatedSegment);

		eventPublisher.publish(event);

		return translatedSegment;
	}

}
