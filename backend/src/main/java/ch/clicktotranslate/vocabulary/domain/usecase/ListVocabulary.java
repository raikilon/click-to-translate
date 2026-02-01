package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepository;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepository;
import ch.clicktotranslate.vocabulary.domain.usecase.input.ListVocabularyInput;
import ch.clicktotranslate.vocabulary.domain.usecase.output.VocabularyItemOutput;
import java.util.List;

public class ListVocabulary {
	private final LemmaRepository lemmaRepository;
	private final UsageRepository usageRepository;

	public ListVocabulary(LemmaRepository lemmaRepository, UsageRepository usageRepository) {
		this.lemmaRepository = lemmaRepository;
		this.usageRepository = usageRepository;
	}

	public List<VocabularyItemOutput> execute(ListVocabularyInput input) {
		return null;
	}
}
