package ch.clicktotranslate.vocabulary.domain;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUpdate;

import java.util.Optional;

public class UpdateLemma {

	private final LemmaRepository lemmaRepository;

	public UpdateLemma(LemmaRepository lemmaRepository) {
		this.lemmaRepository = lemmaRepository;
	}

	public void execute(LemmaUpdate update) {
		Optional<Lemma> existing = lemmaRepository.findByUserIdAndId(update.userId(), update.lemmaId());
		if (existing.isEmpty()) {
			throw new IllegalStateException("Lemma not found.");
		}
		Lemma lemma = existing.get();
		lemma.updateLemma(update.lemma(), update.lemmaTranslation());
		lemmaRepository.save(lemma);
	}

}
