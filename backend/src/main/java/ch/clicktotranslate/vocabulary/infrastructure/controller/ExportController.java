package ch.clicktotranslate.vocabulary.infrastructure.controller;

import ch.clicktotranslate.vocabulary.domain.usecase.ExportVocabulary;
import ch.clicktotranslate.vocabulary.domain.usecase.model.ExportRowOutput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.ExportVocabularyInput;
import java.util.List;

public class ExportController {
	private final ExportVocabulary exportVocabulary;

	public ExportController(ExportVocabulary exportVocabulary) {
		this.exportVocabulary = exportVocabulary;
	}

	public List<ExportRowOutput> export(ExportVocabularyInput input) {
		return exportVocabulary.execute(input);
	}
}
