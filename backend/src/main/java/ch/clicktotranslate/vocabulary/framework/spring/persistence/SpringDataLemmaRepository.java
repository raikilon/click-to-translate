package ch.clicktotranslate.vocabulary.framework.spring.persistence;

import ch.clicktotranslate.vocabulary.framework.spring.persistence.entity.JpaLemmaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataLemmaRepository extends JpaRepository<JpaLemmaEntity, Long> {
}
