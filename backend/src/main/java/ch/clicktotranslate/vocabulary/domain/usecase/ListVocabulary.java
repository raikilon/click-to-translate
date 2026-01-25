package ch.clicktotranslate.vocabulary.domain.usecase;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.usecase.model.ListVocabularyInput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.VocabularyItemOutput;
import java.util.List;

public class ListVocabulary {
	private final LemmaRepositoryGateway lemmaRepositoryGateway;
	private final UsageRepositoryGateway usageRepositoryGateway;

	public ListVocabulary(LemmaRepositoryGateway lemmaRepositoryGateway, UsageRepositoryGateway usageRepositoryGateway) {
		this.lemmaRepositoryGateway = lemmaRepositoryGateway;
		this.usageRepositoryGateway = usageRepositoryGateway;
	}

	public List<VocabularyItemOutput> execute(ListVocabularyInput input) {
		return null;
	}
}
