package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import java.util.List;

import ch.clicktotranslate.vocabulary.domain.usecase.model.VocabularyItemOutput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpVocabItemResponse;

public class HttpVocabularyItemResponseMapper {
	public List<HttpVocabItemResponse> map(List<VocabularyItemOutput> responses) {
		if (responses == null || responses.isEmpty()) {
			return List.of();
		}
		return responses.stream().map(response -> new HttpVocabItemResponse()).toList();
	}
}
