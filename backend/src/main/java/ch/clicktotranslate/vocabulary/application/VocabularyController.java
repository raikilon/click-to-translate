package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.EntryItem;
import ch.clicktotranslate.vocabulary.domain.FormItem;
import ch.clicktotranslate.vocabulary.domain.TranslationUpdate;
import java.util.List;

public class VocabularyController {

	private final ListEntries listEntries;

	private final ListEntriesByLanguage listEntriesByLanguage;

	private final SearchEntries searchEntries;

	private final ListEntryForms listEntryForms;

	private final SetEntryTranslation setEntryTranslation;

	private final DeleteEntry deleteEntry;

	private final ClearEntriesByLanguage clearEntriesByLanguage;

	public VocabularyController(ListEntries listEntries, ListEntriesByLanguage listEntriesByLanguage,
			SearchEntries searchEntries, ListEntryForms listEntryForms, SetEntryTranslation setEntryTranslation,
			DeleteEntry deleteEntry, ClearEntriesByLanguage clearEntriesByLanguage) {
		this.listEntries = listEntries;
		this.listEntriesByLanguage = listEntriesByLanguage;
		this.searchEntries = searchEntries;
		this.listEntryForms = listEntryForms;
		this.setEntryTranslation = setEntryTranslation;
		this.deleteEntry = deleteEntry;
		this.clearEntriesByLanguage = clearEntriesByLanguage;
	}

	public List<EntryItem> listAll() {
		return listEntries.execute();
	}

	public List<EntryItem> listBySourceLanguage(Language sourceLanguage) {
		return listEntriesByLanguage.execute(sourceLanguage);
	}

	public List<EntryItem> search(String query) {
		return searchEntries.execute(query);
	}

	public List<FormItem> listForms(Long entryId) {
		return listEntryForms.execute(entryId);
	}

	public void setTranslationPreference(TranslationUpdate update) {
		setEntryTranslation.execute(update);
	}

	public void deleteEntry(Long entryId) {
		deleteEntry.execute(entryId);
	}

	public void clearBySourceLanguage(Language sourceLanguage) {
		clearEntriesByLanguage.execute(sourceLanguage);
	}

}

