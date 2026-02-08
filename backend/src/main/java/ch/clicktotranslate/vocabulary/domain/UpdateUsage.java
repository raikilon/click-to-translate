package ch.clicktotranslate.vocabulary.domain;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageUpdate;
import ch.clicktotranslate.vocabulary.domain.entity.Usage;
import java.util.Optional;

public class UpdateUsage {

	private final LemmaRepository lemmaRepository;

	public UpdateUsage(LemmaRepository lemmaRepository) {
		this.lemmaRepository = lemmaRepository;
	}

	public void execute(LemmaUsageUpdate update) {
		Optional<Lemma> lemma = lemmaRepository.findByUserIdAndUsageId(update.userId(), update.usageId());
		if (lemma.isEmpty()) {
			throw new IllegalStateException("Usage not found.");
		}
		Lemma aggregate = lemma.get();
		Usage existing = aggregate.usages()
			.stream()
			.filter(usage -> usage.id().equals(update.usageId()))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("Usage not found."));
		Usage updated = new Usage(existing.id(), existing.lemmaId(), update.word(), update.wordTranslation(),
				update.usage(), update.usageTranslation());
		aggregate.replaceUsage(updated);
		lemmaRepository.save(aggregate);
	}

}
