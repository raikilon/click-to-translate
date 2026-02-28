package ch.clicktotranslate.segment.application;

import ch.clicktotranslate.segment.domain.Segment;
import org.jmolecules.ddd.annotation.Service;

@Service
public class SegmentBundleTranslationController {

	private final TranslateSegmentBundle translateSegmentBundle;

	public SegmentBundleTranslationController(TranslateSegmentBundle translateSegmentBundle) {
		this.translateSegmentBundle = translateSegmentBundle;
	}

	public Segment translate(SegmentBundle segmentBundle) {
		return translateSegmentBundle.translate(segmentBundle);
	}

}
