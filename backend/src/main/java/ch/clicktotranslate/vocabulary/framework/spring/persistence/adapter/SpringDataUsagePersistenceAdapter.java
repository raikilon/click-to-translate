package ch.clicktotranslate.vocabulary.framework.spring.persistence.adapter;

import ch.clicktotranslate.vocabulary.framework.spring.persistence.SpringDataUsageRepository;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper.UsageJpaMapper;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.model.UsageRecord;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.store.UsagePersistenceStore;
import java.util.List;
import java.util.Optional;

public class SpringDataUsagePersistenceAdapter implements UsagePersistenceStore {
	private final SpringDataUsageRepository springDataUsageRepository;
	private final UsageJpaMapper usageJpaMapper;

	public SpringDataUsagePersistenceAdapter(SpringDataUsageRepository springDataUsageRepository,
			UsageJpaMapper usageJpaMapper) {
		this.springDataUsageRepository = springDataUsageRepository;
		this.usageJpaMapper = usageJpaMapper;
	}

	@Override
	public Optional<UsageRecord> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public List<UsageRecord> findAll() {
		return List.of();
	}

	@Override
	public UsageRecord store(UsageRecord record) {
		return record;
	}

	@Override
	public void deleteById(Long id) {
	}

	@Override
	public void deleteAll() {
	}
}
