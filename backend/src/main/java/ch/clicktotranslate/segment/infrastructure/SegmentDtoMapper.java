package ch.clicktotranslate.segment.infrastructure;

import ch.clicktotranslate.segment.application.SegmentBundle;
import ch.clicktotranslate.segment.domain.Segment;

public class SegmentDtoMapper {

	public TranslatedSegmentDto toDto(Segment response) {
		return new TranslatedSegmentDto(response.translatedWord());
	}

	public SegmentBundle toDomain(SegmentBundleDto request) {
		return new SegmentBundle(request.word(), request.sentence(), toLanguageCode(request.sourceLanguage()),
				toLanguageCode(request.targetLanguage()), request.occurredAt());
	}

	private String toLanguageCode(LanguageDto languageDto) {
		if (languageDto == null) {
			return null;
		}

		return languageDto.name();
	}

}
