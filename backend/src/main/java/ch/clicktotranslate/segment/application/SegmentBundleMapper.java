package ch.clicktotranslate.segment.application;

import ch.clicktotranslate.segment.domain.Segment;

public class SegmentBundleMapper {

	public Segment map(SegmentBundle segmentBundle) {
		return new Segment(segmentBundle.word(), segmentBundle.sentence(), segmentBundle.sourceLanguage(),
				segmentBundle.targetLanguage());
	}

}
