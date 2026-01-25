package ch.clicktotranslate.vocabulary.infrastructure.persistence.store;

import ch.clicktotranslate.vocabulary.infrastructure.gateway.model.UsageRecord;
import java.util.List;
import java.util.Optional;

public interface UsagePersistenceStore {
	Optional<UsageRecord> findById(Long id);

	List<UsageRecord> findAll();

	UsageRecord store(UsageRecord record);

	void deleteById(Long id);

	void deleteAll();
}
