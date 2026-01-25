package ch.clicktotranslate.vocabulary.infrastructure.gateway;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepositoryGateway;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper.LemmaMapper;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.model.LemmaRecord;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.store.LemmaPersistenceStore;
import java.util.List;
import java.util.Optional;

public class JpaLemmaRepositoryAdapter implements LemmaRepositoryGateway {
	private final LemmaPersistenceStore lemmaPersistenceStore;
	private final LemmaMapper lemmaMapper;

	public JpaLemmaRepositoryAdapter(LemmaPersistenceStore lemmaPersistenceStore, LemmaMapper lemmaMapper) {
		this.lemmaPersistenceStore = lemmaPersistenceStore;
		this.lemmaMapper = lemmaMapper;
	}

	@Override
	public Optional<Lemma> findById(Long id) {
		return lemmaPersistenceStore.findById(id).map(lemmaMapper::toDomain);
	}

	@Override
	public List<Lemma> findAll() {
		return lemmaPersistenceStore.findAll().stream().map(lemmaMapper::toDomain).toList();
	}

	@Override
	public Lemma save(Lemma lemma) {
		LemmaRecord record = lemmaMapper.toRecord(lemma);
		LemmaRecord savedRecord = lemmaPersistenceStore.store(record);
		return lemmaMapper.toDomain(savedRecord);
	}

	@Override
	public void deleteById(Long id) {
		lemmaPersistenceStore.deleteById(id);
	}

	@Override
	public void deleteAll() {
		lemmaPersistenceStore.deleteAll();
	}
}
