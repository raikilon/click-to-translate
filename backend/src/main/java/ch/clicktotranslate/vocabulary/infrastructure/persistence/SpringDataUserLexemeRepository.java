package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUserLexemeRepository extends JpaRepository<JpaUserLexemeEntity, Long> {

	Optional<JpaUserLexemeEntity> findByUserIdAndLexemeId(String userId, Long lexemeId);

	Optional<JpaUserLexemeEntity> findByIdAndUserId(Long id, String userId);

	List<JpaUserLexemeEntity> findAllByUserId(String userId);

	List<JpaUserLexemeEntity> findAllByUserIdAndLexemeLanguage(String userId, String sourceLanguage);

	@Query("""
			select distinct ul
			from JpaUserLexemeEntity ul
			left join JpaSurfaceFormEntity sf on sf.lexeme.id = ul.lexeme.id
			left join JpaUsageEntity usage on usage.userLexeme.id = ul.id
			where ul.userId = :userId
			and (
				lower(ul.lexeme.lemma) like :normalizedQuery
				or lower(sf.form) like :normalizedQuery
				or lower(usage.translatedToken) like :normalizedQuery
			)
			""")
	List<JpaUserLexemeEntity> searchByUserId(@Param("userId") String userId,
			@Param("normalizedQuery") String normalizedQuery);

}
