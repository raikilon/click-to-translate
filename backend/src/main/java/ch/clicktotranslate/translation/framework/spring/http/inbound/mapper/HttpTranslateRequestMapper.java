package ch.clicktotranslate.translation.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.framework.spring.http.inbound.dto.TranslateRequestDto;
import ch.clicktotranslate.translation.infrastructure.controller.model.TranslateRequest;

public class HttpTranslateRequestMapper {
	public TranslateRequest map(TranslateRequestDto request) {
		TranslateRequest mapped = new TranslateRequest();
		mapped.setUserId(request.getUserId());
		mapped.setWord(request.getWord());
		mapped.setSentence(request.getSentence());
		mapped.setSourceLanguage(request.getSourceLanguage());
		mapped.setTargetLanguage(request.getTargetLanguage());
		return mapped;
	}
}
