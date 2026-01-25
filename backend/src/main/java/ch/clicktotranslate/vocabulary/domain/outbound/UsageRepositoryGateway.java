package ch.clicktotranslate.vocabulary.domain.outbound;

import ch.clicktotranslate.vocabulary.domain.entity.Usage;
import java.util.List;
import java.util.Optional;

public interface UsageRepositoryGateway {
	Optional<Usage> findById(Long id);

	List<Usage> findAll();

	Usage save(Usage usage);

	void deleteById(Long id);

	void deleteAll();
}
