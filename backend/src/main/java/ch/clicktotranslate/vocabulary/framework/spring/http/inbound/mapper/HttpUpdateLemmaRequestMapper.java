package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.vocabulary.domain.usecase.model.UpdateLemmaInput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpVocabUpdateRequest;

public class HttpUpdateLemmaRequestMapper {
	public UpdateLemmaInput map(HttpVocabUpdateRequest request) {
		return new UpdateLemmaInput();
	}
}
