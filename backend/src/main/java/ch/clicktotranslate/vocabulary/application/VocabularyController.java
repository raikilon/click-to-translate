package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.Entry;
import java.util.List;

public class VocabularyController {

	private final ListEntries listEntries;

	private final ListEntriesByLanguage listEntriesByLanguage;

	private final SearchEntries searchEntries;

	private final UpdateEntryTranslation updateEntryTranslation;

	private final UpdateEntry updateEntry;

	private final DeleteEntry deleteEntry;

	public VocabularyController(ListEntries listEntries, ListEntriesByLanguage listEntriesByLanguage,
			SearchEntries searchEntries, UpdateEntryTranslation updateEntryTranslation, UpdateEntry updateEntry,
			DeleteEntry deleteEntry) {
		this.listEntries = listEntries;
		this.listEntriesByLanguage = listEntriesByLanguage;
		this.searchEntries = searchEntries;
		this.updateEntryTranslation = updateEntryTranslation;
		this.updateEntry = updateEntry;
		this.deleteEntry = deleteEntry;
	}

	public PageResult<Entry> listAll(PageRequest pageRequest) {
		return listEntries.execute(pageRequest);
	}

	public List<Entry> listByLanguage(Language sourceLanguage) {
		return listEntriesByLanguage.execute(sourceLanguage);
	}

	public List<Entry> search(String query) {
		return searchEntries.execute(query);
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
