package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.vocabulary.domain.usecase.input.UpdateLemmaInput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpVocabUpdateRequest;

public class HttpUpdateLemmaRequestMapper {

	public UpdateLemmaInput map(HttpVocabUpdateRequest request) {
		return new UpdateLemmaInput();
	}

}
