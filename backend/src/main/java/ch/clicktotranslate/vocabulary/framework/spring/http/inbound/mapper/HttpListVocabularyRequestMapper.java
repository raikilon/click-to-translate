package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.vocabulary.domain.usecase.model.ListVocabularyInput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpVocabQueryRequest;

public class HttpListVocabularyRequestMapper {
	public ListVocabularyInput map(HttpVocabQueryRequest request) {
		return new ListVocabularyInput();
	}
}
