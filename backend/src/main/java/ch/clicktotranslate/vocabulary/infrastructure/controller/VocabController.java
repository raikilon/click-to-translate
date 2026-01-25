package ch.clicktotranslate.vocabulary.infrastructure.controller;

import ch.clicktotranslate.vocabulary.domain.usecase.ClearVocabulary;
import ch.clicktotranslate.vocabulary.domain.usecase.DeleteLemma;
import ch.clicktotranslate.vocabulary.domain.usecase.ListVocabulary;
import ch.clicktotranslate.vocabulary.domain.usecase.UpdateLemma;
import ch.clicktotranslate.vocabulary.domain.usecase.model.ClearVocabularyInput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.DeleteLemmaInput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.ListVocabularyInput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.UpdateLemmaInput;
import ch.clicktotranslate.vocabulary.domain.usecase.model.VocabularyItemOutput;
import java.util.List;

public class VocabController {
	private final ListVocabulary listVocabulary;
	private final UpdateLemma updateLemma;
	private final DeleteLemma deleteLemma;
	private final ClearVocabulary clearVocabulary;

	public VocabController(ListVocabulary listVocabulary, UpdateLemma updateLemma, DeleteLemma deleteLemma,
			ClearVocabulary clearVocabulary) {
		this.listVocabulary = listVocabulary;
		this.updateLemma = updateLemma;
		this.deleteLemma = deleteLemma;
		this.clearVocabulary = clearVocabulary;
	}

	public List<VocabularyItemOutput> list(ListVocabularyInput input) {
		return listVocabulary.execute(input);
	}

	public void update(UpdateLemmaInput input) {
		updateLemma.execute(input);
	}

	public void delete(DeleteLemmaInput input) {
		deleteLemma.execute(input);
	}

	public void clear(ClearVocabularyInput input) {
		clearVocabulary.execute(input);
	}
}
