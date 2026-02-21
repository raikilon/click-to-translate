package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataEntryRepository extends JpaRepository<JpaEntryEntity, Long> {

	Optional<JpaEntryEntity> findByIdAndUserId(Long id, String userId);

	Optional<JpaEntryEntity> findWithUsagesByIdAndUserId(Long id, String userId);

	Optional<JpaEntryEntity> findWithUsagesByUserIdAndLanguageAndTerm(String userId, String language,
			String term);

	List<EntryDataProjection> findEntryDataByUserIdOrderByIdAsc(String userId);

	List<EntryDataProjection> findEntryDataByUserIdAndLanguageOrderByIdAsc(String userId, String language);

	List<EntryDataProjection> findEntryDataByUserIdAndTermContainingIgnoreCaseOrderByIdAsc(String userId,
			String term);

}
