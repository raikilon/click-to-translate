package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.usecase.model.UpdateLemmaInput;

public class UpdateLemma {
	private final LemmaRepositoryGateway lemmaRepositoryGateway;

	public UpdateLemma(LemmaRepositoryGateway lemmaRepositoryGateway) {
		this.lemmaRepositoryGateway = lemmaRepositoryGateway;
	}

	public void execute(UpdateLemmaInput input) {
	}
}
