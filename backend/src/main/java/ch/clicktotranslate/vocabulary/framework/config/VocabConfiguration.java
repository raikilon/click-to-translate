package ch.clicktotranslate.vocabulary.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.clicktotranslate.translation.framework.intermodule.inbound.TranslationFacade;
import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepository;
import ch.clicktotranslate.vocabulary.domain.outbound.Lemmatizer;
import ch.clicktotranslate.vocabulary.domain.outbound.TranslationService;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepository;
import ch.clicktotranslate.vocabulary.domain.usecase.AddWordManually;
import ch.clicktotranslate.vocabulary.domain.usecase.ClearVocabulary;
import ch.clicktotranslate.vocabulary.domain.usecase.DeleteLemma;
import ch.clicktotranslate.vocabulary.domain.usecase.ExportVocabulary;
import ch.clicktotranslate.vocabulary.domain.usecase.ListVocabulary;
import ch.clicktotranslate.vocabulary.domain.usecase.RegisterUsageFromTranslation;
import ch.clicktotranslate.vocabulary.domain.usecase.ResolveTempRef;
import ch.clicktotranslate.vocabulary.domain.usecase.UpdateLemma;
import ch.clicktotranslate.vocabulary.framework.spring.events.inbound.SpringTranslatedWordEventListener;
import ch.clicktotranslate.vocabulary.framework.spring.events.inbound.mapper.SpringTranslatedWordEventMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.ExportHttpController;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.VocabHttpController;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpAddWordManuallyRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpAddWordManuallyResponseMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpClearVocabularyRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpDeleteLemmaRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpExportVocabularyRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpListVocabularyRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpResolveWordLinkTokenRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpResolveWordLinkTokenResponseMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper.HttpUpdateLemmaRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.outbound.SpringTranslationApiClient;
import ch.clicktotranslate.vocabulary.framework.spring.http.outbound.mapper.TranslateResponseToTranslationResponseMapper;
import ch.clicktotranslate.vocabulary.framework.spring.http.outbound.mapper.TranslationRequestToTranslateRequestMapper;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.JpaLemmaRepository;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.JpaUsageRepository;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.SpringDataLemmaRepository;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.SpringDataUsageRepository;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper.LemmaJpaMapper;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper.UsageJpaMapper;
import ch.clicktotranslate.vocabulary.infrastructure.controller.ExportController;
import ch.clicktotranslate.vocabulary.infrastructure.controller.VocabController;
import ch.clicktotranslate.vocabulary.infrastructure.event.TranslatedWordEventHandler;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.ClickToTranslateTranslationService;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.ClickToTranslateTranslationServiceApiClient;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.lemmatization.SimpleRuleBasedLemmatizer;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper.LemmaToTranslationRequestMapper;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper.TranslationResponseToTranslatedLemmaMapper;

@Configuration
public class VocabConfiguration {

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
    public LemmaRepository lemmaPersistenceStore(SpringDataLemmaRepository springDataLemmaRepository,
            LemmaJpaMapper lemmaJpaMapper) {
        return new JpaLemmaRepository(springDataLemmaRepository, lemmaJpaMapper);
    }

    @Bean
    public UsageRepository usagePersistenceStore(SpringDataUsageRepository springDataUsageRepository,
            UsageJpaMapper usageJpaMapper) {
        return new JpaUsageRepository(springDataUsageRepository, usageJpaMapper);
    }

    @Bean
    public RegisterUsageFromTranslation registerUsageFromTranslation(Lemmatizer lemmatizer,
            LemmaRepository lemmaRepository, UsageRepository usageRepository) {
        return new RegisterUsageFromTranslation(lemmatizer, lemmaRepository, usageRepository);
    }

    @Bean
    public ListVocabulary listVocabulary(LemmaRepository lemmaRepository,
            UsageRepository usageRepository) {
        return new ListVocabulary(lemmaRepository, usageRepository);
    }

    @Bean
    public UpdateLemma updateLemma(LemmaRepository lemmaRepository) {
        return new UpdateLemma(lemmaRepository);
    }

    @Bean
    public DeleteLemma deleteLemma(LemmaRepository lemmaRepository,
            UsageRepository usageRepository) {
        return new DeleteLemma(lemmaRepository, usageRepository);
    }

    @Bean
    public ClearVocabulary clearVocabulary(LemmaRepository lemmaRepository,
            UsageRepository usageRepository) {
        return new ClearVocabulary(lemmaRepository, usageRepository);
    }

    @Bean
    public ExportVocabulary exportVocabulary(LemmaRepository lemmaRepository,
            UsageRepository usageRepository) {
        return new ExportVocabulary(lemmaRepository, usageRepository);
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
    public HttpResolveWordLinkTokenRequestMapper httpResolveTempRefRequestMapper() {
        return new HttpResolveWordLinkTokenRequestMapper();
    }

    @Bean
    public HttpResolveWordLinkTokenResponseMapper httpResolveTempRefResponseMapper() {
        return new HttpResolveWordLinkTokenResponseMapper();
    }

    @Bean
    public HttpAddWordManuallyRequestMapper httpAddWordManuallyRequestMapper() {
        return new HttpAddWordManuallyRequestMapper();
    }

    @Bean
    public HttpAddWordManuallyResponseMapper httpAddWordManuallyResponseMapper() {
        return new HttpAddWordManuallyResponseMapper();
    }

    // Infrastructure layer mappers (domain <-> infrastructure DTOs)
    @Bean
    public LemmaToTranslationRequestMapper lemmaToTranslationRequestMapper() {
        return new LemmaToTranslationRequestMapper();
    }

    @Bean
    public TranslationResponseToTranslatedLemmaMapper translationResponseToTranslatedLemmaMapper() {
        return new TranslationResponseToTranslatedLemmaMapper();
    }

    // Framework layer mappers (infrastructure DTOs <-> translation module DTOs)
    @Bean
    public TranslationRequestToTranslateRequestMapper translationRequestToTranslateRequestMapper() {
        return new TranslationRequestToTranslateRequestMapper();
    }

    @Bean
    public TranslateResponseToTranslationResponseMapper translateResponseToTranslationResponseMapper() {
        return new TranslateResponseToTranslationResponseMapper();
    }

    @Bean
    public ClickToTranslateTranslationServiceApiClient clickToTranslateTranslationServiceApiClient(
            TranslationFacade translationFacade,
            TranslationRequestToTranslateRequestMapper requestMapper,
            TranslateResponseToTranslationResponseMapper responseMapper) {
        return new SpringTranslationApiClient(translationFacade, requestMapper, responseMapper);
    }

    @Bean
    public TranslationService translationService(
            ClickToTranslateTranslationServiceApiClient apiClient,
            LemmaToTranslationRequestMapper requestMapper,
            TranslationResponseToTranslatedLemmaMapper responseMapper) {
        return new ClickToTranslateTranslationService(apiClient, requestMapper, responseMapper);
    }

    @Bean
    public ResolveTempRef resolveTempRef(LemmaRepository lemmaRepository) {
        return new ResolveTempRef();
    }

    @Bean
    public AddWordManually addWordManually(TranslationService translationService, LemmaRepository lemmaRepository) {
        return new AddWordManually();
    }

    @Bean
    public VocabController vocabController(ListVocabulary listVocabulary, UpdateLemma updateLemma,
            DeleteLemma deleteLemma, ClearVocabulary clearVocabulary, ResolveTempRef resolveTempRef,
            AddWordManually addWordManually) {
        return new VocabController(listVocabulary, updateLemma, deleteLemma, clearVocabulary, resolveTempRef,
                addWordManually);
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
            HttpListVocabularyRequestMapper listRequestMapper,
            HttpUpdateLemmaRequestMapper updateRequestMapper, HttpDeleteLemmaRequestMapper deleteRequestMapper,
            HttpClearVocabularyRequestMapper clearRequestMapper,
            HttpResolveWordLinkTokenRequestMapper resolveTempRefRequestMapper,
            HttpResolveWordLinkTokenResponseMapper resolveTempRefResponseMapper,
            HttpAddWordManuallyRequestMapper addWordManuallyRequestMapper,
            HttpAddWordManuallyResponseMapper addWordManuallyResponseMapper) {
        return new VocabHttpController(vocabController, listRequestMapper, updateRequestMapper,
                deleteRequestMapper, clearRequestMapper, resolveTempRefRequestMapper,
                resolveTempRefResponseMapper, addWordManuallyRequestMapper, addWordManuallyResponseMapper);
    }

    @Bean
    public ExportHttpController exportHttpController(ExportController exportController,
            HttpExportVocabularyRequestMapper requestMapper) {
        return new ExportHttpController(exportController, requestMapper);
    }
}
