package ch.clicktotranslate.translation.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.framework.spring.http.inbound.dto.TranslateRequestDto;

public class HttpTranslateRequestMapper {
	public TranslateWord map(TranslateRequestDto request) {
		return new TranslateWord();
	}
}
