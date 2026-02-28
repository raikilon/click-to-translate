package ch.clicktotranslate.vocabulary.infrastructure.config;

import ch.clicktotranslate.auth.UserProvider;
import ch.clicktotranslate.vocabulary.application.DeleteEntry;
import ch.clicktotranslate.vocabulary.application.DeleteUsage;
import ch.clicktotranslate.vocabulary.application.EntryQuery;
import ch.clicktotranslate.vocabulary.application.GetEntry;
import ch.clicktotranslate.vocabulary.application.ListEntries;
import ch.clicktotranslate.vocabulary.application.ListEntriesByLanguage;
import ch.clicktotranslate.vocabulary.application.ListEntryUsages;
import ch.clicktotranslate.vocabulary.application.RegisterSegmentBundle;
import ch.clicktotranslate.vocabulary.application.SearchEntries;
import ch.clicktotranslate.vocabulary.application.StarUsage;
import ch.clicktotranslate.vocabulary.application.UpdateEntry;
import ch.clicktotranslate.vocabulary.application.UpdateEntryTranslation;
import ch.clicktotranslate.vocabulary.application.VocabularyController;
import ch.clicktotranslate.vocabulary.application.VocabularyRepository;
import ch.clicktotranslate.vocabulary.application.VocabularyUsageController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VocabularyApplicationConfiguration {

	@Bean
	public RegisterSegmentBundle registerSegmentBundle(VocabularyRepository vocabularyRepository) {
		return new RegisterSegmentBundle(vocabularyRepository);
	}

	@Bean
	public ListEntries listEntries(EntryQuery entryQuery, UserProvider userProvider) {
		return new ListEntries(entryQuery, userProvider);
	}

	@Bean
	public ListEntriesByLanguage listEntriesByLanguage(EntryQuery entryQuery, UserProvider userProvider) {
		return new ListEntriesByLanguage(entryQuery, userProvider);
	}

	@Bean
	public SearchEntries searchEntries(EntryQuery entryQuery, UserProvider userProvider) {
		return new SearchEntries(entryQuery, userProvider);
	}

	@Bean
	public GetEntry getEntry(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new GetEntry(vocabularyRepository, userProvider);
	}

	@Bean
	public UpdateEntryTranslation updateEntryTranslation(VocabularyRepository vocabularyRepository,
			UserProvider userProvider) {
		return new UpdateEntryTranslation(vocabularyRepository, userProvider);
	}

	@Bean
	public UpdateEntry updateEntry(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new UpdateEntry(vocabularyRepository, userProvider);
	}

	@Bean
	public DeleteEntry deleteEntry(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new DeleteEntry(vocabularyRepository, userProvider);
	}

	@Bean
	public ListEntryUsages listEntryUsages(VocabularyRepository vocabularyRepository, EntryQuery entryQuery,
			UserProvider userProvider) {
		return new ListEntryUsages(vocabularyRepository, entryQuery, userProvider);
	}

	@Bean
	public DeleteUsage deleteUsage(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new DeleteUsage(vocabularyRepository, userProvider);
	}

	@Bean
	public StarUsage starUsage(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new StarUsage(vocabularyRepository, userProvider);
	}

	@Bean
	public VocabularyController vocabularyController(ListEntries listEntries,
			ListEntriesByLanguage listEntriesByLanguage, SearchEntries searchEntries, GetEntry getEntry,
			UpdateEntryTranslation updateEntryTranslation, UpdateEntry updateEntry, DeleteEntry deleteEntry) {
		return new VocabularyController(listEntries, listEntriesByLanguage, searchEntries, getEntry,
				updateEntryTranslation, updateEntry, deleteEntry);
	}

	@Bean
	public VocabularyUsageController vocabularyUsageController(ListEntryUsages listEntryUsages, DeleteUsage deleteUsage,
			StarUsage starUsage) {
		return new VocabularyUsageController(listEntryUsages, deleteUsage, starUsage);
	}

}
