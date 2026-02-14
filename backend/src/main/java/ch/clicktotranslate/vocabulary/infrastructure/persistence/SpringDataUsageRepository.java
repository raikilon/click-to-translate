package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUsageRepository extends JpaRepository<JpaUsageEntity, Long> {

	Optional<JpaUsageEntity> findByIdAndUserLexemeUserId(Long id, String userId);

	List<JpaUsageEntity> findAllByUserLexemeIdAndUserLexemeUserIdOrderByIdAsc(Long userLexemeId, String userId);

	Optional<JpaUsageEntity> findFirstByUserLexemeIdAndUserLexemeUserIdOrderByIdAsc(Long userLexemeId, String userId);

	void deleteAllByUserLexemeIdAndUserLexemeUserId(Long userLexemeId, String userId);

}
