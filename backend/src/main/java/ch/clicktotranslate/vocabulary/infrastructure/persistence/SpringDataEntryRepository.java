package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataEntryRepository extends JpaRepository<JpaEntryEntity, Long> {

	Optional<JpaEntryEntity> findByIdAndUserId(Long id, String userId);

	Optional<JpaEntryEntity> findByUserIdAndLanguageAndTerm(String userId, String language, String term);

	Page<JpaEntryEntity> findByUserId(String userId, Pageable pageable);

	Page<JpaEntryEntity> findByUserIdAndLanguage(String userId, String language, Pageable pageable);

	Page<JpaEntryEntity> findByUserIdAndTermContainingIgnoreCase(String userId, String term, Pageable pageable);

	boolean existsByIdAndUserId(Long id, String userId);

}
