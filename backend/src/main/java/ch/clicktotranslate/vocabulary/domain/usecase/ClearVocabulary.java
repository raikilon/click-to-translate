package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.usecase.model.ClearVocabularyInput;

public class ClearVocabulary {
	private final LemmaRepositoryGateway lemmaRepositoryGateway;
	private final UsageRepositoryGateway usageRepositoryGateway;

	public ClearVocabulary(LemmaRepositoryGateway lemmaRepositoryGateway, UsageRepositoryGateway usageRepositoryGateway) {
		this.lemmaRepositoryGateway = lemmaRepositoryGateway;
		this.usageRepositoryGateway = usageRepositoryGateway;
	}

	public void execute(ClearVocabularyInput input) {
	}
}
