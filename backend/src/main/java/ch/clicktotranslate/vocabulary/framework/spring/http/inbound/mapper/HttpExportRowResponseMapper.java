package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import java.util.List;

import ch.clicktotranslate.vocabulary.domain.usecase.model.ExportRowOutput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpExportRowResponse;

public class HttpExportRowResponseMapper {
	public List<HttpExportRowResponse> map(List<ExportRowOutput> responses) {
		if (responses == null || responses.isEmpty()) {
			return List.of();
		}
		return responses.stream().map(response -> new HttpExportRowResponse()).toList();
	}
}
