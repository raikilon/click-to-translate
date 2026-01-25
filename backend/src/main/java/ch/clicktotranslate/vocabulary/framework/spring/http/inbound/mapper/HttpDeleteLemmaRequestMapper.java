package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.vocabulary.domain.usecase.model.DeleteLemmaInput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpVocabUpdateRequest;

public class HttpDeleteLemmaRequestMapper {
	public DeleteLemmaInput map(HttpVocabUpdateRequest request) {
		return new DeleteLemmaInput();
	}
}
