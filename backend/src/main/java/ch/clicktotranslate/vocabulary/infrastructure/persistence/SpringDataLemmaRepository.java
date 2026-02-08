package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataLemmaRepository extends JpaRepository<JpaLemmaEntity, Long> {

	Optional<JpaLemmaEntity> findByUserIdAndLemma(String userId, String lemma);

	Optional<JpaLemmaEntity> findByUserIdAndId(String userId, Long id);

	Optional<JpaLemmaEntity> findByUsagesIdAndUserId(Long usageId, String userId);

	List<JpaLemmaEntity> findAllByUserId(String userId);

	List<JpaLemmaEntity> findAllByUserIdAndSourceLanguage(String userId, String sourceLanguage);

	void deleteAllByIdIn(List<Long> ids);

}
