package ch.clicktotranslate.vocabulary.domain.outbound;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import java.util.List;
import java.util.Optional;

public interface LemmaRepositoryGateway {
	Optional<Lemma> findById(Long id);

	List<Lemma> findAll();

	Lemma save(Lemma lemma);

	void deleteById(Long id);

	void deleteAll();
}
