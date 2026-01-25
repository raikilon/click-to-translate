package ch.clicktotranslate.translation.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordOutput;
import ch.clicktotranslate.translation.framework.spring.http.inbound.dto.HttpTranslateResponse;

public class HttpTranslateResponseMapper {
	public HttpTranslateResponse map(TranslateWordOutput response) {
		return new HttpTranslateResponse();
	}
}
