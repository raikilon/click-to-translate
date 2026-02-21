package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUsageRepository extends JpaRepository<JpaUsageEntity, Long> {

	Optional<JpaUsageEntity> findFirstByEntryIdOrderByIdDesc(Long entryId);

	Page<JpaUsageEntity> findByEntryIdAndEntryUserId(Long entryId, String userId, Pageable pageable);

}
