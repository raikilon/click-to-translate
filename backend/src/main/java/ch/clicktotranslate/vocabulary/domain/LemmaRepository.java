package ch.clicktotranslate.vocabulary.domain;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import java.util.List;
import java.util.Optional;

public interface LemmaRepository {

	Optional<Lemma> findByUserIdAndLemma(String userId, String lemma);

	Optional<Lemma> findByUserIdAndId(String userId, Long id);

	Optional<Lemma> findByUserIdAndUsageId(String userId, Long usageId);

	List<Lemma> findAllByUserId(String userId);

	List<Lemma> findAllByUserIdAndSourceLanguage(String userId, Language sourceLanguage);

	Lemma save(Lemma lemma);

	void deleteById(Long id);

	void deleteAllByIdIn(List<Long> ids);

}
