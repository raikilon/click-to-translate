package ch.clicktotranslate.vocabulary.framework.spring.http.inbound;

import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.*;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.*;
import ch.clicktotranslate.vocabulary.infrastructure.controller.VocabController;

import java.util.List;

public class VocabHttpController {

	private final VocabController vocabController;

	private final HttpListVocabularyRequestMapper listRequestMapper;

	private final HttpUpdateLemmaRequestMapper updateRequestMapper;

	private final HttpDeleteLemmaRequestMapper deleteRequestMapper;

	private final HttpClearVocabularyRequestMapper clearRequestMapper;

	private final HttpResolveWordLinkTokenRequestMapper resolveTempRefRequestMapper;

	private final HttpResolveWordLinkTokenResponseMapper resolveTempRefResponseMapper;

	private final HttpAddWordManuallyRequestMapper addWordManuallyRequestMapper;

	private final HttpAddWordManuallyResponseMapper addWordManuallyResponseMapper;

	public VocabHttpController(VocabController vocabController, HttpListVocabularyRequestMapper listRequestMapper,
			HttpUpdateLemmaRequestMapper updateRequestMapper, HttpDeleteLemmaRequestMapper deleteRequestMapper,
			HttpClearVocabularyRequestMapper clearRequestMapper,
			HttpResolveWordLinkTokenRequestMapper resolveTempRefRequestMapper,
			HttpResolveWordLinkTokenResponseMapper resolveTempRefResponseMapper,
			HttpAddWordManuallyRequestMapper addWordManuallyRequestMapper,
			HttpAddWordManuallyResponseMapper addWordManuallyResponseMapper) {
		this.vocabController = vocabController;
		this.listRequestMapper = listRequestMapper;
		this.updateRequestMapper = updateRequestMapper;
		this.deleteRequestMapper = deleteRequestMapper;
		this.clearRequestMapper = clearRequestMapper;
		this.resolveTempRefRequestMapper = resolveTempRefRequestMapper;
		this.resolveTempRefResponseMapper = resolveTempRefResponseMapper;
		this.addWordManuallyRequestMapper = addWordManuallyRequestMapper;
		this.addWordManuallyResponseMapper = addWordManuallyResponseMapper;
	}

	public List<HttpVocabItemResponse> list(HttpVocabQueryRequest request) {
		return null;
	}

	public void update(HttpVocabUpdateRequest request) {
		return;
	}

	public void delete(HttpVocabUpdateRequest request) {
		return;
	}

	public void clear(HttpVocabUpdateRequest request) {
		return;
	}

	public HttpResolveWorkLinkTokenResponse resolveTempRef(HttpResolveWordLinkTokenRequest request) {
		return null;
	}

	public HttpAddWordManuallyResponse addWordManually(HttpAddWordManuallyRequest request) {
		return null;
	}

}
