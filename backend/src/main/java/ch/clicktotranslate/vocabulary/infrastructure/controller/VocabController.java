package ch.clicktotranslate.vocabulary.infrastructure.controller;

import ch.clicktotranslate.vocabulary.domain.usecase.ClearVocabulary;
import ch.clicktotranslate.vocabulary.domain.usecase.DeleteLemma;
import ch.clicktotranslate.vocabulary.domain.usecase.ListVocabulary;
import ch.clicktotranslate.vocabulary.domain.usecase.UpdateLemma;
import ch.clicktotranslate.vocabulary.domain.usecase.ResolveTempRef;
import ch.clicktotranslate.vocabulary.domain.usecase.AddWordManually;
import ch.clicktotranslate.vocabulary.domain.usecase.input.ClearVocabularyInput;
import ch.clicktotranslate.vocabulary.domain.usecase.input.DeleteLemmaInput;
import ch.clicktotranslate.vocabulary.domain.usecase.input.ListVocabularyInput;
import ch.clicktotranslate.vocabulary.domain.usecase.input.UpdateLemmaInput;
import ch.clicktotranslate.vocabulary.domain.usecase.output.VocabularyItemOutput;
import ch.clicktotranslate.vocabulary.domain.usecase.input.ResolveTempRefInput;
import ch.clicktotranslate.vocabulary.domain.usecase.output.ResolveTempRefOutput;
import ch.clicktotranslate.vocabulary.domain.usecase.input.AddWordManuallyInput;
import ch.clicktotranslate.vocabulary.domain.usecase.output.AddWordManuallyOutput;
import java.util.List;

public class VocabController {

	private final ListVocabulary listVocabulary;

	private final UpdateLemma updateLemma;

	private final DeleteLemma deleteLemma;

	private final ClearVocabulary clearVocabulary;

	private final ResolveTempRef resolveTempRef;

	private final AddWordManually addWordManually;

	public VocabController(ListVocabulary listVocabulary, UpdateLemma updateLemma, DeleteLemma deleteLemma,
			ClearVocabulary clearVocabulary, ResolveTempRef resolveTempRef, AddWordManually addWordManually) {
		this.listVocabulary = listVocabulary;
		this.updateLemma = updateLemma;
		this.deleteLemma = deleteLemma;
		this.clearVocabulary = clearVocabulary;
		this.resolveTempRef = resolveTempRef;
		this.addWordManually = addWordManually;
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

	public ResolveTempRefOutput resolveTempRef(ResolveTempRefInput input) {
		return resolveTempRef.execute(input);
	}

	public AddWordManuallyOutput addWordManually(AddWordManuallyInput input) {
		return addWordManually.execute(input);
	}

}
