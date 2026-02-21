package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.Entry;

public class VocabularyController {

	private final ListEntries listEntries;

	private final ListEntriesByLanguage listEntriesByLanguage;

	private final SearchEntries searchEntries;

	private final GetEntry getEntry;

	private final UpdateEntryTranslation updateEntryTranslation;

	private final UpdateEntry updateEntry;

	private final DeleteEntry deleteEntry;

	public VocabularyController(ListEntries listEntries, ListEntriesByLanguage listEntriesByLanguage,
			SearchEntries searchEntries, GetEntry getEntry, UpdateEntryTranslation updateEntryTranslation,
			UpdateEntry updateEntry, DeleteEntry deleteEntry) {
		this.listEntries = listEntries;
		this.listEntriesByLanguage = listEntriesByLanguage;
		this.searchEntries = searchEntries;
		this.getEntry = getEntry;
		this.updateEntryTranslation = updateEntryTranslation;
		this.updateEntry = updateEntry;
		this.deleteEntry = deleteEntry;
	}

	public PageResult<Entry> listAll(PageRequest pageRequest) {
		return listEntries.execute(pageRequest);
	}

	public PageResult<Entry> listByLanguage(Language sourceLanguage, PageRequest pageRequest) {
		return listEntriesByLanguage.execute(sourceLanguage, pageRequest);
	}

	public PageResult<Entry> search(String query, PageRequest pageRequest) {
		return searchEntries.execute(query, pageRequest);
	}

	public Entry getEntry(Long entryId) {
		return getEntry.execute(entryId);
	}

	public void updateTranslation(TranslationUpdate update) {
		updateEntryTranslation.execute(update);
	}

	public void updateTerm(UpdateTerm update) {
		updateEntry.execute(update);
	}

	public void deleteEntry(Long entryId) {
		deleteEntry.execute(entryId);
	}

}
