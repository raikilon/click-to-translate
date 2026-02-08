package ch.clicktotranslate.vocabulary.domain;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaDeletion;

import java.util.Optional;

public class DeleteLemma {

	private final LemmaRepository lemmaRepository;

	public DeleteLemma(LemmaRepository lemmaRepository) {
		this.lemmaRepository = lemmaRepository;
	}

	public void execute(LemmaDeletion deletion) {
		Optional<Lemma> lemma = lemmaRepository.findByUserIdAndId(deletion.userId(), deletion.lemmaId());
		if (lemma.isEmpty()) {
			return;
		}
		lemmaRepository.deleteById(deletion.lemmaId());
	}

}
