package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.vocabulary.domain.usecase.model.ExportVocabularyInput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpExportRequest;

public class HttpExportVocabularyRequestMapper {
	public ExportVocabularyInput map(HttpExportRequest request) {
		return new ExportVocabularyInput();
	}
}
