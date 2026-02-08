package ch.clicktotranslate.vocabulary.application.web;

import ch.clicktotranslate.vocabulary.domain.ClearVocabulary;
import ch.clicktotranslate.vocabulary.domain.DeleteLemma;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaDeletion;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUpdate;
import ch.clicktotranslate.vocabulary.domain.ListVocabulary;
import ch.clicktotranslate.vocabulary.domain.UpdateLemma;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyClear;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyItem;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyQuery;
import java.util.List;

public class LemmaController {

	private final ListVocabulary listVocabulary;

	private final UpdateLemma updateLemma;

	private final DeleteLemma deleteLemma;

	private final ClearVocabulary clearVocabulary;

	public LemmaController(ListVocabulary listVocabulary, UpdateLemma updateLemma, DeleteLemma deleteLemma,
			ClearVocabulary clearVocabulary) {
		this.listVocabulary = listVocabulary;
		this.updateLemma = updateLemma;
		this.deleteLemma = deleteLemma;
		this.clearVocabulary = clearVocabulary;
	}

	public List<VocabularyItem> list(VocabularyQuery query) {
		return listVocabulary.execute(query);
	}

	public void update(LemmaUpdate update) {
		updateLemma.execute(update);
	}

	public void delete(LemmaDeletion delete) {
		deleteLemma.execute(delete);
	}

	public void clear(VocabularyClear clear) {
		clearVocabulary.execute(clear);
	}

}
