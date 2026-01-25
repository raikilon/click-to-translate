package ch.clicktotranslate.vocabulary.infrastructure.persistence.store;

import ch.clicktotranslate.vocabulary.infrastructure.gateway.model.LemmaRecord;
import java.util.List;
import java.util.Optional;

public interface LemmaPersistenceStore {
	Optional<LemmaRecord> findById(Long id);

	List<LemmaRecord> findAll();

	LemmaRecord store(LemmaRecord record);

	void deleteById(Long id);

	void deleteAll();
}
