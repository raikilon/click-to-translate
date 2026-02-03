package ch.clicktotranslate.translation.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.translation.domain.entity.TranslatedWord;
import ch.clicktotranslate.translation.framework.spring.http.inbound.dto.TranslateResponseDto;

public class HttpTranslateResponseMapper {
	public TranslateResponseDto map(TranslatedWord response) {
		TranslateResponseDto mapped = new TranslateResponseDto();
		mapped.setWord(response.getWord());
		mapped.setSentence(response.getSentence());
		mapped.setWordTranslation(response.getWordTranslation());
		mapped.setSentenceTranslation(response.getSentenceTranslation());
		return mapped;
	}
}
