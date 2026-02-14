package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUserLexemeTranslationRepository extends JpaRepository<JpaUserLexemeTranslationEntity, Long> {

	Optional<JpaUserLexemeTranslationEntity> findFirstByUserIdAndSourceLexemeIdAndTargetLanguage(String userId,
			Long sourceLexemeId, String targetLanguage);

	Optional<JpaUserLexemeTranslationEntity> findFirstByUserIdAndSourceLexemeIdOrderByIdAsc(String userId,
			Long sourceLexemeId);

}
