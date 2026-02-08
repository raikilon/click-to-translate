package ch.clicktotranslate.vocabulary.domain;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyClear;

import java.util.List;

public class ClearVocabulary {

	private final LemmaRepository lemmaRepository;

	public ClearVocabulary(LemmaRepository lemmaRepository) {
		this.lemmaRepository = lemmaRepository;
	}

	public void execute(VocabularyClear clear) {
		List<Lemma> lemmas = clear.sourceLanguage() == null ? lemmaRepository.findAllByUserId(clear.userId())
				: lemmaRepository.findAllByUserIdAndSourceLanguage(clear.userId(), clear.sourceLanguage());
		List<Long> lemmaIds = lemmas.stream().map(Lemma::id).toList();
		lemmaRepository.deleteAllByIdIn(lemmaIds);
	}

}
