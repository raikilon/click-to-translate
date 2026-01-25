package ch.clicktotranslate.vocabulary.infrastructure.gateway;

import ch.clicktotranslate.vocabulary.domain.entity.Usage;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepositoryGateway;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper.UsageMapper;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.model.UsageRecord;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.store.UsagePersistenceStore;
import java.util.List;
import java.util.Optional;

public class JpaUsageRepositoryAdapter implements UsageRepositoryGateway {
	private final UsagePersistenceStore usagePersistenceStore;
	private final UsageMapper usageMapper;

	public JpaUsageRepositoryAdapter(UsagePersistenceStore usagePersistenceStore, UsageMapper usageMapper) {
		this.usagePersistenceStore = usagePersistenceStore;
		this.usageMapper = usageMapper;
	}

	@Override
	public Optional<Usage> findById(Long id) {
		return usagePersistenceStore.findById(id).map(usageMapper::toDomain);
	}

	@Override
	public List<Usage> findAll() {
		return usagePersistenceStore.findAll().stream().map(usageMapper::toDomain).toList();
	}

	@Override
	public Usage save(Usage usage) {
		UsageRecord record = usageMapper.toRecord(usage);
		UsageRecord savedRecord = usagePersistenceStore.store(record);
		return usageMapper.toDomain(savedRecord);
	}

	@Override
	public void deleteById(Long id) {
		usagePersistenceStore.deleteById(id);
	}

	@Override
	public void deleteAll() {
		usagePersistenceStore.deleteAll();
	}
}
