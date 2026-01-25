package ch.clicktotranslate.vocabulary.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.outbound.Lemmatizer;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepositoryGateway;
import ch.clicktotranslate.vocabulary.domain.usecase.ClearVocabulary;
import ch.clicktotranslate.vocabulary.domain.usecase.DeleteLemma;
import ch.clicktotranslate.vocabulary.domain.usecase.ExportVocabulary;
import ch.clicktotranslate.vocabulary.domain.usecase.ListVocabulary;
import ch.clicktotranslate.vocabulary.domain.usecase.RegisterUsageFromTranslation;
import ch.clicktotranslate.vocabulary.domain.usecase.UpdateLemma;
import ch.clicktotranslate.vocabulary.framework.export.CsvExportWriter;
import ch.clicktotranslate.vocabulary.framework.lemmatization.SimpleRuleBasedLemmatizer;
import ch.clicktotranslate.vocabulary.framework.spring.events.inbound.SpringTranslatedWordEventListener;
import ch.clicktotranslate.vocabulary.framework.spring.events.inbound.mapper.SpringTranslatedWordEventMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.ExportHttpController;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.VocabHttpController;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpClearVocabularyRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpDeleteLemmaRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpExportRowResponseMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpExportVocabularyRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpListVocabularyRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpUpdateLemmaRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpVocabularyItemResponseMapper;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.SpringDataLemmaRepository;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.SpringDataUsageRepository;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.adapter.SpringDataLemmaPersistenceAdapter;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.adapter.SpringDataUsagePersistenceAdapter;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper.LemmaJpaMapper;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper.UsageJpaMapper;
import ch.clicktotranslate.vocabulary.infrastructure.controller.ExportController;
import ch.clicktotranslate.vocabulary.infrastructure.controller.VocabController;
import ch.clicktotranslate.vocabulary.infrastructure.event.TranslatedWordEventHandler;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.JpaLemmaRepositoryAdapter;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.JpaUsageRepositoryAdapter;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper.LemmaMapper;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper.UsageMapper;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.store.LemmaPersistenceStore;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.store.UsagePersistenceStore;

@Configuration
public class VocabConfiguration {
	@Bean
	public LemmaMapper lemmaMapper() {
		return new LemmaMapper();
	}

	@Bean
	public UsageMapper usageMapper() {
		return new UsageMapper();
	}

	@Bean
	public CsvExportWriter csvExportWriter() {
		return new CsvExportWriter();
	}

	@Bean
	public Lemmatizer lemmatizer() {
		return new SimpleRuleBasedLemmatizer();
	}

	@Bean
	public LemmaJpaMapper lemmaJpaMapper() {
		return new LemmaJpaMapper();
	}

	@Bean
	public UsageJpaMapper usageJpaMapper() {
		return new UsageJpaMapper();
	}

	@Bean
	public LemmaPersistenceStore lemmaPersistenceStore(SpringDataLemmaRepository springDataLemmaRepository,
			LemmaJpaMapper lemmaJpaMapper) {
		return new SpringDataLemmaPersistenceAdapter(springDataLemmaRepository, lemmaJpaMapper);
	}

	@Bean
	public UsagePersistenceStore usagePersistenceStore(SpringDataUsageRepository springDataUsageRepository,
			UsageJpaMapper usageJpaMapper) {
		return new SpringDataUsagePersistenceAdapter(springDataUsageRepository, usageJpaMapper);
	}

	@Bean
	public LemmaRepositoryGateway lemmaRepositoryGateway(LemmaPersistenceStore lemmaPersistenceStore,
			LemmaMapper lemmaMapper) {
		return new JpaLemmaRepositoryAdapter(lemmaPersistenceStore, lemmaMapper);
	}

	@Bean
	public UsageRepositoryGateway usageRepositoryGateway(UsagePersistenceStore usagePersistenceStore,
			UsageMapper usageMapper) {
		return new JpaUsageRepositoryAdapter(usagePersistenceStore, usageMapper);
	}

	@Bean
	public RegisterUsageFromTranslation registerUsageFromTranslation(Lemmatizer lemmatizer,
			LemmaRepositoryGateway lemmaRepositoryGateway, UsageRepositoryGateway usageRepositoryGateway) {
		return new RegisterUsageFromTranslation(lemmatizer, lemmaRepositoryGateway, usageRepositoryGateway);
	}

	@Bean
	public ListVocabulary listVocabulary(LemmaRepositoryGateway lemmaRepositoryGateway,
			UsageRepositoryGateway usageRepositoryGateway) {
		return new ListVocabulary(lemmaRepositoryGateway, usageRepositoryGateway);
	}

	@Bean
	public UpdateLemma updateLemma(LemmaRepositoryGateway lemmaRepositoryGateway) {
		return new UpdateLemma(lemmaRepositoryGateway);
	}

	@Bean
	public DeleteLemma deleteLemma(LemmaRepositoryGateway lemmaRepositoryGateway,
			UsageRepositoryGateway usageRepositoryGateway) {
		return new DeleteLemma(lemmaRepositoryGateway, usageRepositoryGateway);
	}

	@Bean
	public ClearVocabulary clearVocabulary(LemmaRepositoryGateway lemmaRepositoryGateway,
			UsageRepositoryGateway usageRepositoryGateway) {
		return new ClearVocabulary(lemmaRepositoryGateway, usageRepositoryGateway);
	}

	@Bean
	public ExportVocabulary exportVocabulary(LemmaRepositoryGateway lemmaRepositoryGateway,
			UsageRepositoryGateway usageRepositoryGateway) {
		return new ExportVocabulary(lemmaRepositoryGateway, usageRepositoryGateway);
	}

	@Bean
	public SpringTranslatedWordEventMapper springTranslatedWordEventMapper() {
		return new SpringTranslatedWordEventMapper();
	}

	@Bean
	public TranslatedWordEventHandler translatedWordEventHandler(RegisterUsageFromTranslation registerUsageFromTranslation) {
		return new TranslatedWordEventHandler(registerUsageFromTranslation);
	}

	@Bean
	public HttpListVocabularyRequestMapper httpListVocabularyRequestMapper() {
		return new HttpListVocabularyRequestMapper();
	}

	@Bean
	public HttpVocabularyItemResponseMapper httpVocabularyItemResponseMapper() {
		return new HttpVocabularyItemResponseMapper();
	}

	@Bean
	public HttpUpdateLemmaRequestMapper httpUpdateLemmaRequestMapper() {
		return new HttpUpdateLemmaRequestMapper();
	}

	@Bean
	public HttpDeleteLemmaRequestMapper httpDeleteLemmaRequestMapper() {
		return new HttpDeleteLemmaRequestMapper();
	}

	@Bean
	public HttpClearVocabularyRequestMapper httpClearVocabularyRequestMapper() {
		return new HttpClearVocabularyRequestMapper();
	}

	@Bean
	public HttpExportVocabularyRequestMapper httpExportVocabularyRequestMapper() {
		return new HttpExportVocabularyRequestMapper();
	}

	@Bean
	public HttpExportRowResponseMapper httpExportRowResponseMapper() {
		return new HttpExportRowResponseMapper();
	}

	@Bean
	public VocabController vocabController(ListVocabulary listVocabulary, UpdateLemma updateLemma,
			DeleteLemma deleteLemma, ClearVocabulary clearVocabulary) {
		return new VocabController(listVocabulary, updateLemma, deleteLemma, clearVocabulary);
	}

	@Bean
	public ExportController exportController(ExportVocabulary exportVocabulary) {
		return new ExportController(exportVocabulary);
	}

	@Bean
	public SpringTranslatedWordEventListener springTranslatedWordEventListener(
			SpringTranslatedWordEventMapper eventMapper, TranslatedWordEventHandler eventHandler) {
		return new SpringTranslatedWordEventListener(eventMapper, eventHandler);
	}

	@Bean
	public VocabHttpController vocabHttpController(VocabController vocabController,
			HttpListVocabularyRequestMapper listRequestMapper, HttpVocabularyItemResponseMapper itemResponseMapper,
			HttpUpdateLemmaRequestMapper updateRequestMapper, HttpDeleteLemmaRequestMapper deleteRequestMapper,
			HttpClearVocabularyRequestMapper clearRequestMapper) {
		return new VocabHttpController(vocabController, listRequestMapper, itemResponseMapper, updateRequestMapper,
				deleteRequestMapper, clearRequestMapper);
	}

	@Bean
	public ExportHttpController exportHttpController(ExportController exportController,
			HttpExportVocabularyRequestMapper requestMapper, HttpExportRowResponseMapper responseMapper) {
		return new ExportHttpController(exportController, requestMapper, responseMapper);
	}
}
