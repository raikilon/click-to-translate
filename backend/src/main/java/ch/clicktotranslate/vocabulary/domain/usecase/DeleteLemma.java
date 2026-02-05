package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepository;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepository;
import ch.clicktotranslate.vocabulary.domain.usecase.input.DeleteLemmaInput;

public class DeleteLemma {

	private final LemmaRepository lemmaRepository;

	private final UsageRepository usageRepository;

	public DeleteLemma(LemmaRepository lemmaRepository, UsageRepository usageRepository) {
		this.lemmaRepository = lemmaRepository;
		this.usageRepository = usageRepository;
	}

	public void execute(DeleteLemmaInput input) {
	}

}
