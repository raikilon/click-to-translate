package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepository;
import ch.clicktotranslate.vocabulary.domain.usecase.input.UpdateLemmaInput;

public class UpdateLemma {
	private final LemmaRepository lemmaRepository;

	public UpdateLemma(LemmaRepository lemmaRepository) {
		this.lemmaRepository = lemmaRepository;
	}

	public void execute(UpdateLemmaInput input) {
	}
}
