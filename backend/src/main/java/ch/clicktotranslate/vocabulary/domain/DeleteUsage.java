package ch.clicktotranslate.vocabulary.domain;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageDeletion;

import java.util.Optional;

public class DeleteUsage {

	private final LemmaRepository lemmaRepository;

	public DeleteUsage(LemmaRepository lemmaRepository) {
		this.lemmaRepository = lemmaRepository;
	}

	public void execute(LemmaUsageDeletion deletion) {
		Optional<Lemma> lemma = lemmaRepository.findByUserIdAndUsageId(deletion.userId(), deletion.usageId());
		if (lemma.isEmpty()) {
			return;
		}
		Lemma aggregate = lemma.get();
		boolean removed = aggregate.removeUsage(deletion.usageId());
		if (removed) {
			lemmaRepository.save(aggregate);
		}
	}

}
