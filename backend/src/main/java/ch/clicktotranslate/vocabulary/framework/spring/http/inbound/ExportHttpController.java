package ch.clicktotranslate.vocabulary.framework.spring.http.inbound;

import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpExportRequest;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpExportRowResponse;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpExportVocabularyRequestMapper;
import ch.clicktotranslate.vocabulary.infrastructure.controller.ExportController;

import java.util.List;

public class ExportHttpController {
  private final ExportController exportController;
  private final HttpExportVocabularyRequestMapper requestMapper;

  public ExportHttpController(ExportController exportController, HttpExportVocabularyRequestMapper requestMapper) {
    this.exportController = exportController;
    this.requestMapper = requestMapper;
  }

  public List<HttpExportRowResponse> export(HttpExportRequest request) {
    return null;
  }
}
