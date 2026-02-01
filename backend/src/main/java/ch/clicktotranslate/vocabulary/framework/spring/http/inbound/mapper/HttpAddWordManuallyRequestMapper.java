package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.vocabulary.domain.usecase.input.AddWordManuallyInput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpAddWordManuallyRequest;

public class HttpAddWordManuallyRequestMapper {
	public AddWordManuallyInput map(HttpAddWordManuallyRequest request) {
		return new AddWordManuallyInput();
	}
}
