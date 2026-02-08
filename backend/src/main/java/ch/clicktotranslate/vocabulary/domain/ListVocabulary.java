package ch.clicktotranslate.vocabulary.domain;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyItem;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyQuery;

import java.util.ArrayList;
import java.util.List;

public class ListVocabulary {

	private final LemmaRepository lemmaRepository;

	public ListVocabulary(LemmaRepository lemmaRepository) {
		this.lemmaRepository = lemmaRepository;
	}

	public List<VocabularyItem> execute(VocabularyQuery query) {
		List<Lemma> lemmas = lemmaRepository.findAllByUserId(query.userId());
		if (lemmas.isEmpty()) {
			return List.of();
		}
		List<VocabularyItem> output = new ArrayList<>();
		for (Lemma lemma : lemmas) {
			output.add(new VocabularyItem(lemma.id(), lemma.lemma(), lemma.lemmaTranslation(), lemma.sourceLanguage(),
					lemma.targetLanguage()));
		}
		return output;
	}

}
