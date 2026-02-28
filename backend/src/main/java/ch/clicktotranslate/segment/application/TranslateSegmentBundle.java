package ch.clicktotranslate.segment.application;

import ch.clicktotranslate.auth.UserProvider;
import ch.clicktotranslate.segment.domain.Segment;
import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;

public class TranslateSegmentBundle {

	private final SegmentTranslatorService segmentTranslatorService;

	private final EventPublisher eventPublisher;

	private final SegmentBundleCreatedEventMapper segmentBundleCreatedEventMapper;

	private final SegmentBundleMapper segmentBundleMapper;

	private final UserProvider userProvider;

	public TranslateSegmentBundle(SegmentTranslatorService segmentTranslatorService, EventPublisher eventPublisher,
			SegmentBundleCreatedEventMapper segmentBundleCreatedEventMapper, SegmentBundleMapper segmentBundleMapper,
			UserProvider userProvider) {
		this.segmentTranslatorService = segmentTranslatorService;
		this.eventPublisher = eventPublisher;
		this.segmentBundleCreatedEventMapper = segmentBundleCreatedEventMapper;
		this.segmentBundleMapper = segmentBundleMapper;
		this.userProvider = userProvider;
	}

	public Segment translate(SegmentBundle segmentBundle) {
		String userId = userProvider.currentUserId().value();
		Segment segment = segmentBundleMapper.map(segmentBundle);
		Segment translatedSegment = segmentTranslatorService.translate(segment);
		SegmentBundleCreatedEvent event = segmentBundleCreatedEventMapper.map(userId, segmentBundle, translatedSegment);
		eventPublisher.publish(event);
		return translatedSegment;
	}

}
