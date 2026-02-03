package ch.clicktotranslate.translation.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.translation.domain.entity.TranslatedWord;
import ch.clicktotranslate.translation.framework.spring.http.inbound.dto.TranslateResponseDto;

public class HttpTranslateResponseMapper {
	public TranslateResponseDto map(TranslatedWord response) {
		return new TranslateResponseDto(
				response.word(),
				response.sentence(),
				response.wordTranslation(),
				response.sentenceTranslation()
		);
	}
}
