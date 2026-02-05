package ch.clicktotranslate.translation.application.web;

import ch.clicktotranslate.translation.domain.Segment;

public class SegmentBundleMapper {

	public Segment map(SegmentBundle request) {
		return new Segment(request.word(), request.sentence(), request.sourceLanguage(), request.targetLanguage());
	}

}
