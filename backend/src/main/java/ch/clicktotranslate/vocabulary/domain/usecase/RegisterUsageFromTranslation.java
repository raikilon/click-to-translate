package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepository;
import ch.clicktotranslate.vocabulary.domain.outbound.Lemmatizer;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepository;
import ch.clicktotranslate.vocabulary.domain.usecase.input.RegisterUsageInput;

public class RegisterUsageFromTranslation {

	private final Lemmatizer lemmatizer;

	private final LemmaRepository lemmaRepository;

	private final UsageRepository usageRepository;

	public RegisterUsageFromTranslation(Lemmatizer lemmatizer, LemmaRepository lemmaRepository,
			UsageRepository usageRepository) {
		this.lemmatizer = lemmatizer;
		this.lemmaRepository = lemmaRepository;
		this.usageRepository = usageRepository;
	}

	public void execute(RegisterUsageInput input) {
	}

}
