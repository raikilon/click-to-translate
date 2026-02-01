package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepository;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepository;
import ch.clicktotranslate.vocabulary.domain.usecase.output.ExportRowOutput;
import ch.clicktotranslate.vocabulary.domain.usecase.input.ExportVocabularyInput;
import java.util.List;

public class ExportVocabulary {
	private final LemmaRepository lemmaRepository;
	private final UsageRepository usageRepository;

	public ExportVocabulary(LemmaRepository lemmaRepository, UsageRepository usageRepository) {
		this.lemmaRepository = lemmaRepository;
		this.usageRepository = usageRepository;
	}

	public List<ExportRowOutput> execute(ExportVocabularyInput input) {
		return null;
	}
}
