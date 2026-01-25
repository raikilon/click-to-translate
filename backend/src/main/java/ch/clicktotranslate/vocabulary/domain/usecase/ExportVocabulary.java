package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.usecase.model.ExportRowOutput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.ExportVocabularyInput;
import java.util.List;

public class ExportVocabulary {
	private final LemmaRepositoryGateway lemmaRepositoryGateway;
	private final UsageRepositoryGateway usageRepositoryGateway;

	public ExportVocabulary(LemmaRepositoryGateway lemmaRepositoryGateway, UsageRepositoryGateway usageRepositoryGateway) {
		this.lemmaRepositoryGateway = lemmaRepositoryGateway;
		this.usageRepositoryGateway = usageRepositoryGateway;
	}

	public List<ExportRowOutput> execute(ExportVocabularyInput input) {
		return null;
	}
}
