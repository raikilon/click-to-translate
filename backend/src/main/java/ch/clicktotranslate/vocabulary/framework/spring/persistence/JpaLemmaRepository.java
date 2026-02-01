package ch.clicktotranslate.vocabulary.framework.spring.persistence;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.outbound.LemmaRepository;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper.LemmaJpaMapper;
import java.util.List;
import java.util.Optional;

public class JpaLemmaRepository implements LemmaRepository {
	private final SpringDataLemmaRepository springDataLemmaRepository;
	private final LemmaJpaMapper lemmaJpaMapper;

	public JpaLemmaRepository(SpringDataLemmaRepository springDataLemmaRepository,
                              LemmaJpaMapper lemmaJpaMapper) {
		this.springDataLemmaRepository = springDataLemmaRepository;
		this.lemmaJpaMapper = lemmaJpaMapper;
	}

	@Override
	public Optional<Lemma> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public List<Lemma> findAll() {
		return List.of();
	}

	@Override
	public Lemma save(Lemma record) {
		return record;
	}

	@Override
	public void deleteById(Long id) {
	}

	@Override
	public void deleteAll() {
	}
}
