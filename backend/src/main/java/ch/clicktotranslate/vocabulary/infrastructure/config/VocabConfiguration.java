package ch.clicktotranslate.vocabulary.infrastructure.config;

import ch.clicktotranslate.vocabulary.application.*;
import ch.clicktotranslate.vocabulary.infrastructure.event.SegmentBundleTokenizedEventMapper;
import ch.clicktotranslate.vocabulary.infrastructure.event.SpringSegmentBundleTokenizedEventListener;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.JpaVocabularyRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataEntryRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataUsageRepository;
import ch.clicktotranslate.vocabulary.infrastructure.security.SpringSecurityUserProvider;
import ch.clicktotranslate.vocabulary.infrastructure.web.VocabularyDtoMapper;
import ch.clicktotranslate.vocabulary.infrastructure.web.UsageDtoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VocabConfiguration {

	@Bean
	public JpaVocabularyRepository jpaVocabularyRepository(SpringDataEntryRepository entryRepository,
			SpringDataUsageRepository usageRepository) {
		return new JpaVocabularyRepository(entryRepository, usageRepository);
	}

	@Bean
	public RegisterSegmentBundle registerSegmentBundle(VocabularyRepository vocabularyRepository) {
		return new RegisterSegmentBundle(vocabularyRepository);
	}

	@Bean
	public SegmentBundleTokenizedEventMapper segmentBundleTokenizedEventMapper() {
		return new SegmentBundleTokenizedEventMapper();
	}

	@Bean
	public SpringSegmentBundleTokenizedEventListener springSegmentBundleTokenizedEventListener(
			SegmentBundleTokenizedEventMapper eventMapper, RegisterSegmentBundle registerSegmentBundle) {
		return new SpringSegmentBundleTokenizedEventListener(eventMapper, registerSegmentBundle);
	}

	@Bean
	public UserProvider userProvider() {
		return new SpringSecurityUserProvider();
	}


	@Bean
	public ListEntries listEntries(EntryQuery entryQuery, UserProvider userProvider) {
		return new ListEntries(entryQuery, userProvider);
	}

	@Bean
	public ListEntriesByLanguage listEntriesByLanguage(EntryQuery entryQuery,
			UserProvider userProvider) {
		return new ListEntriesByLanguage(entryQuery, userProvider);
	}

	@Bean
	public SearchEntries searchEntries(EntryQuery entryQuery, UserProvider userProvider) {
		return new SearchEntries(entryQuery, userProvider);
	}

	@Bean
	public UpdateEntryTranslation updateEntryTranslation(VocabularyRepository vocabularyRepository,
                                                         UserProvider userProvider) {
		return new UpdateEntryTranslation(vocabularyRepository, userProvider);
	}

	@Bean
	public UpdateEntry updateEntry(VocabularyRepository vocabularyRepository,
                                   UserProvider userProvider) {
		return new UpdateEntry(vocabularyRepository, userProvider);
	}

	@Bean
	public DeleteEntry deleteEntry(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new DeleteEntry(vocabularyRepository, userProvider);
	}

	@Bean
	public ListEntryUsages listEntryUsages(VocabularyRepository vocabularyRepository,
                                           UserProvider userProvider) {
		return new ListEntryUsages(vocabularyRepository, userProvider);
	}

	@Bean
	public DeleteUsage deleteUsage(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new DeleteUsage(vocabularyRepository, userProvider);
	}

	@Bean
	public VocabularyController vocabularyController(ListEntries listEntries,
			ListEntriesByLanguage listEntriesByLanguage, SearchEntries searchEntries,
			UpdateEntryTranslation updateEntryTranslation,
			UpdateEntry updateEntry,
			DeleteEntry deleteEntry) {
		return new VocabularyController(listEntries, listEntriesByLanguage, searchEntries,
				updateEntryTranslation, updateEntry, deleteEntry);
	}

	@Bean
	public VocabularyUsageController vocabularyUsageController(ListEntryUsages listEntryUsages,
			DeleteUsage deleteUsage) {
		return new VocabularyUsageController(listEntryUsages, deleteUsage);
	}

	@Bean
	public VocabularyDtoMapper vocabularyDtoMapper() {
		return new VocabularyDtoMapper();
	}

	@Bean
	public UsageDtoMapper vocabularyUsageDtoMapper() {
		return new UsageDtoMapper();
	}

}


