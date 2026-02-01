package ch.clicktotranslate.vocabulary.domain;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageItem;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsagesQuery;
import ch.clicktotranslate.vocabulary.domain.entity.Usage;
import java.util.List;
import java.util.Optional;

public class ListLemmaUsages {

	private final LemmaRepository lemmaRepository;

	public ListLemmaUsages(LemmaRepository lemmaRepository) {
		this.lemmaRepository = lemmaRepository;
	}

	public List<LemmaUsageItem> execute(LemmaUsagesQuery query) {
		Optional<Lemma> lemma = lemmaRepository.findByUserIdAndId(query.userId(), query.lemmaId());
		if (lemma.isEmpty()) {
			return List.of();
		}
		return lemma.get().usages().stream().map(this::toItem).toList();
	}

	private LemmaUsageItem toItem(Usage usage) {
		return new LemmaUsageItem(usage.id(), usage.word(), usage.wordTranslation(), usage.usage(),
				usage.usageTranslation());
	}

}
