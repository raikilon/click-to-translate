package ch.clicktotranslate.vocabulary.framework.spring.persistence;

import ch.clicktotranslate.vocabulary.domain.entity.Usage;
import ch.clicktotranslate.vocabulary.domain.outbound.UsageRepository;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper.UsageJpaMapper;
import java.util.List;
import java.util.Optional;

public class JpaUsageRepository implements UsageRepository {

	private final SpringDataUsageRepository springDataUsageRepository;

	private final UsageJpaMapper usageJpaMapper;

	public JpaUsageRepository(SpringDataUsageRepository springDataUsageRepository, UsageJpaMapper usageJpaMapper) {
		this.springDataUsageRepository = springDataUsageRepository;
		this.usageJpaMapper = usageJpaMapper;
	}

	@Override
	public Optional<Usage> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public List<Usage> findAll() {
		return List.of();
	}

	@Override
	public Usage save(Usage record) {
		return record;
	}

	@Override
	public void deleteById(Long id) {
	}

	@Override
	public void deleteAll() {
	}

}
