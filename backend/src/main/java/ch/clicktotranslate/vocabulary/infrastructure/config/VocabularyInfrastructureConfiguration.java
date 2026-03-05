package ch.clicktotranslate.vocabulary.infrastructure.config;

import ch.clicktotranslate.vocabulary.application.RegisterSegmentBundle;
import ch.clicktotranslate.vocabulary.infrastructure.event.SegmentBundleLemmatizedEventMapper;
import ch.clicktotranslate.vocabulary.infrastructure.event.SpringSegmentBundleLemmatizedEventListener;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.JpaVocabularyRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataEntryRepository;
import ch.clicktotranslate.vocabulary.infrastructure.web.PageRequestDtoMapper;
import ch.clicktotranslate.vocabulary.infrastructure.web.VocabularyDtoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VocabularyInfrastructureConfiguration {

	@Bean
	public JpaVocabularyRepository jpaVocabularyRepository(SpringDataEntryRepository entryRepository) {
		return new JpaVocabularyRepository(entryRepository);
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
	public VocabularyDtoMapper vocabularyDtoMapper() {
		return new VocabularyDtoMapper();
	}

	@Bean
	public PageRequestDtoMapper pageRequestDtoMapper() {
		return new PageRequestDtoMapper();
	}

}
