package ch.clicktotranslate.vocabulary.framework.spring.http.inbound;

import ch.clicktotranslate.vocabulary.domain.usecase.model.ExportRowOutput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.ExportVocabularyInput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpExportRequest;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpExportRowResponse;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpExportRowResponseMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpExportVocabularyRequestMapper;
import ch.clicktotranslate.vocabulary.infrastructure.controller.ExportController;
import java.util.List;

public class ExportHttpController {
	private final ExportController exportController;
	private final HttpExportVocabularyRequestMapper requestMapper;
	private final HttpExportRowResponseMapper responseMapper;

	public ExportHttpController(ExportController exportController, HttpExportVocabularyRequestMapper requestMapper,
			HttpExportRowResponseMapper responseMapper) {
		this.exportController = exportController;
		this.requestMapper = requestMapper;
		this.responseMapper = responseMapper;
	}

	public List<HttpExportRowResponse> export(HttpExportRequest request) {
		ExportVocabularyInput input = requestMapper.map(request);
		List<ExportRowOutput> responseModel = exportController.export(input);
		return responseMapper.map(responseModel);
	}
}
