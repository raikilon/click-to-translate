package ch.clicktotranslate.translation.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordInput;
import ch.clicktotranslate.translation.framework.spring.http.inbound.dto.HttpTranslateRequest;

public class HttpTranslateRequestMapper {
	public TranslateWordInput map(HttpTranslateRequest request) {
		return new TranslateWordInput();
	}
}
