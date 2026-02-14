package ch.clicktotranslate.vocabulary.infrastructure.config;

import ch.clicktotranslate.vocabulary.application.RegisterSegmentBundle;
import ch.clicktotranslate.vocabulary.application.UserProvider;
import ch.clicktotranslate.vocabulary.application.ClearEntriesByLanguage;
import ch.clicktotranslate.vocabulary.application.DeleteEntry;
import ch.clicktotranslate.vocabulary.application.DeleteUsage;
import ch.clicktotranslate.vocabulary.application.EntryItems;
import ch.clicktotranslate.vocabulary.application.ListEntries;
import ch.clicktotranslate.vocabulary.application.ListEntriesByLanguage;
import ch.clicktotranslate.vocabulary.application.ListEntryForms;
import ch.clicktotranslate.vocabulary.application.ListEntryUsages;
import ch.clicktotranslate.vocabulary.application.SearchEntries;
import ch.clicktotranslate.vocabulary.application.SetEntryTranslation;
import ch.clicktotranslate.vocabulary.application.UpdateUsage;
import ch.clicktotranslate.vocabulary.application.VocabularyController;
import ch.clicktotranslate.vocabulary.application.VocabularyUsageController;
import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;
import ch.clicktotranslate.vocabulary.infrastructure.event.SegmentBundleTokenizedEventMapper;
import ch.clicktotranslate.vocabulary.infrastructure.event.SpringSegmentBundleTokenizedEventListener;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.JpaVocabularyRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataLexemeRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataSurfaceFormRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataUsageRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataUserLexemeRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataUserLexemeTranslationRepository;
import ch.clicktotranslate.vocabulary.infrastructure.security.SpringSecurityUserProvider;
import ch.clicktotranslate.vocabulary.infrastructure.web.VocabularyDtoMapper;
import ch.clicktotranslate.vocabulary.infrastructure.web.UsageDtoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VocabConfiguration {

	@Bean
	public VocabularyRepository vocabularyRepository(SpringDataLexemeRepository lexemeRepository,
			SpringDataUserLexemeRepository userLexemeRepository, SpringDataSurfaceFormRepository surfaceFormRepository,
			SpringDataUsageRepository usageRepository,
			SpringDataUserLexemeTranslationRepository translationRepository) {
		return new JpaVocabularyRepository(lexemeRepository, userLexemeRepository, surfaceFormRepository,
				usageRepository, translationRepository);
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
	public EntryItems entryItems(VocabularyRepository vocabularyRepository) {
		return new EntryItems(vocabularyRepository);
	}

	@Bean
	public ListEntries listEntries(VocabularyRepository vocabularyRepository, UserProvider userProvider,
			EntryItems entryItems) {
		return new ListEntries(vocabularyRepository, userProvider, entryItems);
	}

	@Bean
	public ListEntriesByLanguage listEntriesByLanguage(VocabularyRepository vocabularyRepository,
			UserProvider userProvider, EntryItems entryItems) {
		return new ListEntriesByLanguage(vocabularyRepository, userProvider, entryItems);
	}

	@Bean
	public SearchEntries searchEntries(VocabularyRepository vocabularyRepository, UserProvider userProvider,
			EntryItems entryItems) {
		return new SearchEntries(vocabularyRepository, userProvider, entryItems);
	}

	@Bean
	public ListEntryForms listEntryForms(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new ListEntryForms(vocabularyRepository, userProvider);
	}

	@Bean
	public SetEntryTranslation setEntryTranslation(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new SetEntryTranslation(vocabularyRepository, userProvider);
	}

	@Bean
	public DeleteEntry deleteEntry(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new DeleteEntry(vocabularyRepository, userProvider);
	}

	@Bean
	public ClearEntriesByLanguage clearEntriesByLanguage(VocabularyRepository vocabularyRepository,
			UserProvider userProvider) {
		return new ClearEntriesByLanguage(vocabularyRepository, userProvider);
	}

	@Bean
	public ListEntryUsages listEntryUsages(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new ListEntryUsages(vocabularyRepository, userProvider);
	}

	@Bean
	public UpdateUsage updateUsage(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new UpdateUsage(vocabularyRepository, userProvider);
	}

	@Bean
	public DeleteUsage deleteUsage(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		return new DeleteUsage(vocabularyRepository, userProvider);
	}

	@Bean
	public VocabularyController vocabularyController(ListEntries listEntries, ListEntriesByLanguage listEntriesByLanguage,
			SearchEntries searchEntries, ListEntryForms listEntryForms, SetEntryTranslation setEntryTranslation,
			DeleteEntry deleteEntry, ClearEntriesByLanguage clearEntriesByLanguage) {
		return new VocabularyController(listEntries, listEntriesByLanguage, searchEntries, listEntryForms,
				setEntryTranslation, deleteEntry, clearEntriesByLanguage);
	}

	@Bean
	public VocabularyUsageController vocabularyUsageController(ListEntryUsages listEntryUsages,
			UpdateUsage updateUsage, DeleteUsage deleteUsage) {
		return new VocabularyUsageController(listEntryUsages, updateUsage, deleteUsage);
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
