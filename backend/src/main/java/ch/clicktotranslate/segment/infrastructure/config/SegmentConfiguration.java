package ch.clicktotranslate.segment.infrastructure.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.clicktotranslate.segment.application.SegmentBundleMapper;
import ch.clicktotranslate.segment.application.SegmentBundleTranslationController;
import ch.clicktotranslate.segment.application.EventPublisher;
import ch.clicktotranslate.segment.application.SegmentBundleCreatedEventMapper;
import ch.clicktotranslate.segment.infrastructure.TextTranslatorClient;
import ch.clicktotranslate.segment.application.SegmentTranslatorService;
import ch.clicktotranslate.segment.application.TextTranslator;
import ch.clicktotranslate.segment.infrastructure.SpringEventPublisher;
import ch.clicktotranslate.segment.infrastructure.SegmentDtoMapper;
import ch.clicktotranslate.translation.infrastructure.TextTranslationFacade;

@Configuration
public class SegmentConfiguration {

	@Bean
	public TextTranslator segmentTranslator(TextTranslationFacade textTranslationFacade) {
		return new TextTranslatorClient(textTranslationFacade);
	}

	@Bean
	public SegmentTranslatorService translateWord(TextTranslator textTranslator) {
		return new SegmentTranslatorService(textTranslator);
	}

	@Bean
	public SegmentBundleTranslationController translateWordController(SegmentTranslatorService segmentTranslatorService,
			EventPublisher eventPublisher, SegmentBundleCreatedEventMapper eventFactory,
			SegmentBundleMapper segmentBundleMapper) {
		return new SegmentBundleTranslationController(segmentTranslatorService, eventPublisher, eventFactory,
				segmentBundleMapper);
	}

	@Bean
	public SegmentBundleMapper translateWordMapper() {
		return new SegmentBundleMapper();
	}

	@Bean
	public SegmentDtoMapper httpTranslateResponseMapper() {
		return new SegmentDtoMapper();
	}

	@Bean
	public SegmentBundleCreatedEventMapper translatedWordEventFactory() {
		return new SegmentBundleCreatedEventMapper();
	}

	@Bean
	public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		return new SpringEventPublisher(applicationEventPublisher);
	}

}
