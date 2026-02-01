package ch.clicktotranslate.vocabulary.framework.spring.persistence;

import ch.clicktotranslate.vocabulary.framework.spring.persistence.entity.JpaUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUsageRepository extends JpaRepository<JpaUsageEntity, Long> {
}
