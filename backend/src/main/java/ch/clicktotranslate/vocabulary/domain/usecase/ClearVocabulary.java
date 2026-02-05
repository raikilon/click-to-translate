package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepository;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepository;
import ch.clicktotranslate.vocabulary.domain.usecase.input.ClearVocabularyInput;

public class ClearVocabulary {

	private final LemmaRepository lemmaRepository;

	private final UsageRepository usageRepository;

	public ClearVocabulary(LemmaRepository lemmaRepository, UsageRepository usageRepository) {
		this.lemmaRepository = lemmaRepository;
		this.usageRepository = usageRepository;
	}

	public void execute(ClearVocabularyInput input) {
	}

}
