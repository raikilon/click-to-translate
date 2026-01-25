package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.outbound.Lemmatizer;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.usecase.model.RegisterUsageInput;

public class RegisterUsageFromTranslation {
	private final Lemmatizer lemmatizer;
	private final LemmaRepositoryGateway lemmaRepositoryGateway;
	private final UsageRepositoryGateway usageRepositoryGateway;

	public RegisterUsageFromTranslation(Lemmatizer lemmatizer, LemmaRepositoryGateway lemmaRepositoryGateway,
			UsageRepositoryGateway usageRepositoryGateway) {
		this.lemmatizer = lemmatizer;
		this.lemmaRepositoryGateway = lemmaRepositoryGateway;
		this.usageRepositoryGateway = usageRepositoryGateway;
	}

	public void execute(RegisterUsageInput input) {
	}
}
