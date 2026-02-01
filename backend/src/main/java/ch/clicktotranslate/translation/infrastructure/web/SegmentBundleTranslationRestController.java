package ch.clicktotranslate.translation.infrastructure.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.clicktotranslate.translation.application.web.SegmentBundleTranslationController;
import ch.clicktotranslate.translation.application.web.SegmentBundle;

@RestController
@RequestMapping("/api/translate")
public class SegmentBundleTranslationRestController {

	private final SegmentBundleTranslationController segmentBundleTranslationController;

	private final SegmentDtoMapper segmentDtoMapper;

	public SegmentBundleTranslationRestController(SegmentBundleTranslationController segmentBundleTranslationController,
			SegmentDtoMapper segmentDtoMapper) {
		this.segmentBundleTranslationController = segmentBundleTranslationController;
		this.segmentDtoMapper = segmentDtoMapper;
	}

	@PostMapping
	public TranslatedSegmentDto translate(@RequestBody SegmentBundleDto SegmentBundleDto) {
		SegmentBundle segmentBundle = segmentDtoMapper.toDomain(SegmentBundleDto);
		ch.clicktotranslate.translation.domain.TranslatedSegment translatedSegment = segmentBundleTranslationController
			.translate(segmentBundle);
		return segmentDtoMapper.toDto(translatedSegment);
	}

}
