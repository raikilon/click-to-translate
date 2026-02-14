package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataSurfaceFormRepository extends JpaRepository<JpaSurfaceFormEntity, Long> {

	Optional<JpaSurfaceFormEntity> findByLexemeIdAndForm(Long lexemeId, String form);

	List<JpaSurfaceFormEntity> findAllByLexemeIdOrderByFormAsc(Long lexemeId);

}
