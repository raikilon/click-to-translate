package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataLexemeRepository extends JpaRepository<JpaLexemeEntity, Long> {

	Optional<JpaLexemeEntity> findByLanguageAndLemma(String language, String lemma);

}
