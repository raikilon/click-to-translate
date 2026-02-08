package ch.clicktotranslate.vocabulary.infrastructure.config;

import ch.clicktotranslate.vocabulary.application.lemmatization.SimpleRuleBasedLemmatizer;
import ch.clicktotranslate.vocabulary.application.web.LemmaController;
import ch.clicktotranslate.vocabulary.application.web.LemmaUsageController;
import ch.clicktotranslate.vocabulary.domain.ClearVocabulary;
import ch.clicktotranslate.vocabulary.domain.DeleteLemma;
import ch.clicktotranslate.vocabulary.domain.DeleteUsage;
import ch.clicktotranslate.vocabulary.domain.LemmaRepository;
import ch.clicktotranslate.vocabulary.domain.Lemmatizer;
import ch.clicktotranslate.vocabulary.domain.ListLemmaUsages;
import ch.clicktotranslate.vocabulary.domain.ListVocabulary;
import ch.clicktotranslate.vocabulary.domain.RegisterUsageFromTranslation;
import ch.clicktotranslate.vocabulary.domain.UpdateLemma;
import ch.clicktotranslate.vocabulary.domain.UpdateUsage;
import ch.clicktotranslate.vocabulary.infrastructure.event.SpringNewSegmentEventListener;
import ch.clicktotranslate.vocabulary.infrastructure.event.SpringNewSegmentEventMapper;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.JpaLemmaRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataLemmaRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.LemmaJpaMapper;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.UsageJpaMapper;
import ch.clicktotranslate.vocabulary.infrastructure.web.LemmaDtoMapper;
import ch.clicktotranslate.vocabulary.infrastructure.web.LemmaRestController;
import ch.clicktotranslate.vocabulary.infrastructure.web.LemmaUsageDtoMapper;
import ch.clicktotranslate.vocabulary.infrastructure.web.LemmaUsageRestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VocabConfiguration {

	@Bean
	public Lemmatizer lemmatizer() {
		return new SimpleRuleBasedLemmatizer();
	}

	@Bean
	public LemmaJpaMapper lemmaJpaMapper(UsageJpaMapper usageJpaMapper) {
		return new LemmaJpaMapper(usageJpaMapper);
	}

	@Bean
	public UsageJpaMapper usageJpaMapper() {
		return new UsageJpaMapper();
	}

	@Bean
	public LemmaRepository lemmaRepository(SpringDataLemmaRepository springDataLemmaRepository,
			LemmaJpaMapper lemmaJpaMapper) {
		return new JpaLemmaRepository(springDataLemmaRepository, lemmaJpaMapper);
	}

	@Bean
	public RegisterUsageFromTranslation registerUsageFromTranslation(Lemmatizer lemmatizer,
			LemmaRepository lemmaRepository) {
		return new RegisterUsageFromTranslation(lemmatizer, lemmaRepository);
	}

	@Bean
	public ListVocabulary listVocabulary(LemmaRepository lemmaRepository) {
		return new ListVocabulary(lemmaRepository);
	}

	@Bean
	public ListLemmaUsages listLemmaUsages(LemmaRepository lemmaRepository) {
		return new ListLemmaUsages(lemmaRepository);
	}

	@Bean
	public UpdateLemma updateLemma(LemmaRepository lemmaRepository) {
		return new UpdateLemma(lemmaRepository);
	}

	@Bean
	public DeleteLemma deleteLemma(LemmaRepository lemmaRepository) {
		return new DeleteLemma(lemmaRepository);
	}

	@Bean
	public UpdateUsage updateUsage(LemmaRepository lemmaRepository) {
		return new UpdateUsage(lemmaRepository);
	}

	@Bean
	public DeleteUsage deleteUsage(LemmaRepository lemmaRepository) {
		return new DeleteUsage(lemmaRepository);
	}

	@Bean
	public ClearVocabulary clearVocabulary(LemmaRepository lemmaRepository) {
		return new ClearVocabulary(lemmaRepository);
	}

	@Bean
	public SpringNewSegmentEventMapper springTranslatedWordEventMapper() {
		return new SpringNewSegmentEventMapper();
	}

	@Bean
	public SpringNewSegmentEventListener springTranslatedWordEventListener(SpringNewSegmentEventMapper eventMapper,
			RegisterUsageFromTranslation registerUsageFromTranslation) {
		return new SpringNewSegmentEventListener(eventMapper, registerUsageFromTranslation);
	}

	@Bean
	public LemmaController lemmaController(ListVocabulary listVocabulary, UpdateLemma updateLemma,
			DeleteLemma deleteLemma, ClearVocabulary clearVocabulary) {
		return new LemmaController(listVocabulary, updateLemma, deleteLemma, clearVocabulary);
	}

	@Bean
	public LemmaUsageController lemmaUsageController(UpdateUsage updateUsage, DeleteUsage deleteUsage,
			ListLemmaUsages listLemmaUsages) {
		return new LemmaUsageController(updateUsage, deleteUsage, listLemmaUsages);
	}

	@Bean
	public LemmaDtoMapper lemmaDtoMapper() {
		return new LemmaDtoMapper();
	}

	@Bean
	public LemmaUsageDtoMapper lemmaUsageDtoMapper() {
		return new LemmaUsageDtoMapper();
	}

	@Bean
	public LemmaRestController lemmaRestController(LemmaController lemmaController, LemmaDtoMapper lemmaDtoMapper) {
		return new LemmaRestController(lemmaController, lemmaDtoMapper);
	}

	@Bean
	public LemmaUsageRestController lemmaUsageRestController(LemmaUsageController lemmaUsageController,
			LemmaUsageDtoMapper lemmaUsageDtoMapper) {
		return new LemmaUsageRestController(lemmaUsageController, lemmaUsageDtoMapper);
	}

}
