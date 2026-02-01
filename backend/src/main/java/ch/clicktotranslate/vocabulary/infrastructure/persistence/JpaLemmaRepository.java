package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.LemmaRepository;
import ch.clicktotranslate.vocabulary.domain.entity.Lemma;

import java.util.List;
import java.util.Optional;

public class JpaLemmaRepository implements LemmaRepository {

	private final SpringDataLemmaRepository springDataLemmaRepository;

	private final LemmaJpaMapper lemmaJpaMapper;

	public JpaLemmaRepository(SpringDataLemmaRepository springDataLemmaRepository, LemmaJpaMapper lemmaJpaMapper) {
		this.springDataLemmaRepository = springDataLemmaRepository;
		this.lemmaJpaMapper = lemmaJpaMapper;
	}

	@Override
	public Optional<Lemma> findByUserIdAndLemma(String userId, String lemma) {
		return springDataLemmaRepository.findByUserIdAndLemma(userId, lemma).map(lemmaJpaMapper::toRecord);
	}

	@Override
	public Optional<Lemma> findByUserIdAndId(String userId, Long id) {
		return springDataLemmaRepository.findByUserIdAndId(userId, id).map(lemmaJpaMapper::toRecord);
	}

	@Override
	public Optional<Lemma> findByUserIdAndUsageId(String userId, Long usageId) {
		return springDataLemmaRepository.findByUsagesIdAndUserId(usageId, userId).map(lemmaJpaMapper::toRecord);
	}

	@Override
	public List<Lemma> findAllByUserId(String userId) {
		return springDataLemmaRepository.findAllByUserId(userId).stream().map(lemmaJpaMapper::toRecord).toList();
	}

	@Override
	public List<Lemma> findAllByUserIdAndSourceLanguage(String userId, Language sourceLanguage) {
		return springDataLemmaRepository.findAllByUserIdAndSourceLanguage(userId, sourceLanguage.name())
			.stream()
			.map(lemmaJpaMapper::toRecord)
			.toList();
	}

	@Override
	public Lemma save(Lemma record) {
		JpaLemmaEntity entity = lemmaJpaMapper.toEntity(record);
		JpaLemmaEntity saved = springDataLemmaRepository.save(entity);
		return lemmaJpaMapper.toRecord(saved);
	}

	@Override
	public void deleteById(Long id) {
		springDataLemmaRepository.deleteById(id);
	}

	@Override
	public void deleteAllByIdIn(List<Long> ids) {
		if (ids.isEmpty()) {
			return;
		}
		springDataLemmaRepository.deleteAllByIdIn(ids);
	}

}
