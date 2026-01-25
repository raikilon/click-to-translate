package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.vocabulary.domain.usecase.model.ClearVocabularyInput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpVocabUpdateRequest;

public class HttpClearVocabularyRequestMapper {
	public ClearVocabularyInput map(HttpVocabUpdateRequest request) {
		return new ClearVocabularyInput();
	}
}
