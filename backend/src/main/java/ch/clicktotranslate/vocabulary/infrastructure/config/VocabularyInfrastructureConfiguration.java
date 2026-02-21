package ch.clicktotranslate.vocabulary.infrastructure.config;

import ch.clicktotranslate.vocabulary.application.RegisterSegmentBundle;
import ch.clicktotranslate.vocabulary.application.UserProvider;
import ch.clicktotranslate.vocabulary.infrastructure.event.SegmentBundleLemmatizedEventMapper;
import ch.clicktotranslate.vocabulary.infrastructure.event.SpringSegmentBundleLemmatizedEventListener;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.JpaVocabularyRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataEntryRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataUsageRepository;
import ch.clicktotranslate.vocabulary.infrastructure.security.SpringSecurityUserProvider;
import ch.clicktotranslate.vocabulary.infrastructure.web.PageRequestDtoMapper;
import ch.clicktotranslate.vocabulary.infrastructure.web.UsageDtoMapper;
import ch.clicktotranslate.vocabulary.infrastructure.web.VocabularyDtoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VocabularyInfrastructureConfiguration {

	@Bean
	public JpaVocabularyRepository jpaVocabularyRepository(SpringDataEntryRepository entryRepository,
			SpringDataUsageRepository usageRepository) {
		return new JpaVocabularyRepository(entryRepository, usageRepository);
	}

	@Bean
	public SegmentBundleLemmatizedEventMapper segmentBundleLemmatizedEventMapper() {
		return new SegmentBundleLemmatizedEventMapper();
	}

	@Bean
	public SpringSegmentBundleLemmatizedEventListener springSegmentBundleLemmatizedEventListener(
			SegmentBundleLemmatizedEventMapper eventMapper, RegisterSegmentBundle registerSegmentBundle) {
		return new SpringSegmentBundleLemmatizedEventListener(eventMapper, registerSegmentBundle);
	}

	@Bean
	public UserProvider userProvider() {
		return new SpringSecurityUserProvider();
	}

	@Bean
	public VocabularyDtoMapper vocabularyDtoMapper() {
		return new VocabularyDtoMapper();
	}

	@Bean
	public UsageDtoMapper vocabularyUsageDtoMapper() {
		return new UsageDtoMapper();
	}

	@Bean
	public PageRequestDtoMapper pageRequestDtoMapper() {
		return new PageRequestDtoMapper();
	}

}
