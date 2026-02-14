package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataEntryRepository extends JpaRepository<JpaEntryEntity, Long> {


	Optional<JpaEntryEntity> findByIdAndUserId(Long id, String userId);

	@EntityGraph(attributePaths = { "usages" })
	Optional<JpaEntryEntity> findWithUsagesByIdAndUserId(Long id, String userId);

	@EntityGraph(attributePaths = { "usages" })
	Optional<JpaEntryEntity> findWithUsagesByUserIdAndSourceLanguageAndSourceLemma(String userId,
			String sourceLanguage, String sourceLemma);

	@EntityGraph(attributePaths = { "usages" })
	List<JpaEntryEntity> findAllByUserIdOrderByIdAsc(String userId);

	@EntityGraph(attributePaths = { "usages" })
	List<JpaEntryEntity> findAllByUserIdAndSourceLanguageOrderByIdAsc(String userId, String sourceLanguage);

	@EntityGraph(attributePaths = { "usages" })
	List<JpaEntryEntity> findAllByUserIdAndSourceLemmaContainingIgnoreCaseOrderByIdAsc(String userId,
			String sourceLemma);

}

