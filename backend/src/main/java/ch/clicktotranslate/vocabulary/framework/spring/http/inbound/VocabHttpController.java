package ch.clicktotranslate.vocabulary.framework.spring.http.inbound;

import ch.clicktotranslate.vocabulary.domain.usecase.model.ClearVocabularyInput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.DeleteLemmaInput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.ListVocabularyInput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.UpdateLemmaInput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.VocabularyItemOutput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpVocabItemResponse;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpVocabQueryRequest;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpVocabUpdateRequest;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpClearVocabularyRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpDeleteLemmaRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpListVocabularyRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpUpdateLemmaRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpVocabularyItemResponseMapper;
import ch.clicktotranslate.vocabulary.infrastructure.controller.VocabController;
import java.util.List;

public class VocabHttpController {
	private final VocabController vocabController;
	private final HttpListVocabularyRequestMapper listRequestMapper;
	private final HttpVocabularyItemResponseMapper itemResponseMapper;
	private final HttpUpdateLemmaRequestMapper updateRequestMapper;
	private final HttpDeleteLemmaRequestMapper deleteRequestMapper;
	private final HttpClearVocabularyRequestMapper clearRequestMapper;

	public VocabHttpController(VocabController vocabController, HttpListVocabularyRequestMapper listRequestMapper,
			HttpVocabularyItemResponseMapper itemResponseMapper, HttpUpdateLemmaRequestMapper updateRequestMapper,
			HttpDeleteLemmaRequestMapper deleteRequestMapper, HttpClearVocabularyRequestMapper clearRequestMapper) {
		this.vocabController = vocabController;
		this.listRequestMapper = listRequestMapper;
		this.itemResponseMapper = itemResponseMapper;
		this.updateRequestMapper = updateRequestMapper;
		this.deleteRequestMapper = deleteRequestMapper;
		this.clearRequestMapper = clearRequestMapper;
	}

	public List<HttpVocabItemResponse> list(HttpVocabQueryRequest request) {
		ListVocabularyInput input = listRequestMapper.map(request);
		List<VocabularyItemOutput> responseModel = vocabController.list(input);
		return itemResponseMapper.map(responseModel);
	}

	public void update(HttpVocabUpdateRequest request) {
		UpdateLemmaInput input = updateRequestMapper.map(request);
		vocabController.update(input);
	}

	public void delete(HttpVocabUpdateRequest request) {
		DeleteLemmaInput input = deleteRequestMapper.map(request);
		vocabController.delete(input);
	}

	public void clear(HttpVocabUpdateRequest request) {
		ClearVocabularyInput input = clearRequestMapper.map(request);
		vocabController.clear(input);
	}
}
