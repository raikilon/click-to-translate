package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.vocabulary.domain.usecase.output.AddWordManuallyOutput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpAddWordManuallyResponse;

public class HttpAddWordManuallyResponseMapper {

	public HttpAddWordManuallyResponse map(AddWordManuallyOutput output) {
		return new HttpAddWordManuallyResponse();
	}

}
