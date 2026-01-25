package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.usecase.model.DeleteLemmaInput;

public class DeleteLemma {
	private final LemmaRepositoryGateway lemmaRepositoryGateway;
	private final UsageRepositoryGateway usageRepositoryGateway;

	public DeleteLemma(LemmaRepositoryGateway lemmaRepositoryGateway, UsageRepositoryGateway usageRepositoryGateway) {
		this.lemmaRepositoryGateway = lemmaRepositoryGateway;
		this.usageRepositoryGateway = usageRepositoryGateway;
	}

	public void execute(DeleteLemmaInput input) {
	}
}
