package ch.clicktotranslate.vocabulary.framework.spring.persistence.adapter;

import ch.clicktotranslate.vocabulary.framework.spring.persistence.SpringDataLemmaRepository;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper.LemmaJpaMapper;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.model.LemmaRecord;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.store.LemmaPersistenceStore;
import java.util.List;
import java.util.Optional;

public class SpringDataLemmaPersistenceAdapter implements LemmaPersistenceStore {
	private final SpringDataLemmaRepository springDataLemmaRepository;
	private final LemmaJpaMapper lemmaJpaMapper;

	public SpringDataLemmaPersistenceAdapter(SpringDataLemmaRepository springDataLemmaRepository,
			LemmaJpaMapper lemmaJpaMapper) {
		this.springDataLemmaRepository = springDataLemmaRepository;
		this.lemmaJpaMapper = lemmaJpaMapper;
	}

	@Override
	public Optional<LemmaRecord> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public List<LemmaRecord> findAll() {
		return List.of();
	}

	@Override
	public LemmaRecord store(LemmaRecord record) {
		return record;
	}

	@Override
	public void deleteById(Long id) {
	}

	@Override
	public void deleteAll() {
	}
}
