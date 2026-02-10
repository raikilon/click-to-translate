package ch.clicktotranslate.segment.infrastructure.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.clicktotranslate.segment.application.SegmentBundleMapper;
import ch.clicktotranslate.segment.application.SegmentBundleTranslationController;
import ch.clicktotranslate.segment.application.EventPublisher;
import ch.clicktotranslate.segment.application.TranslatedWordEventMapper;
import ch.clicktotranslate.segment.infrastructure.TextTranslatorBridge;
import ch.clicktotranslate.segment.domain.SegmentTranslation;
import ch.clicktotranslate.segment.domain.TextTranslator;
import ch.clicktotranslate.segment.infrastructure.event.SpringEventPublisher;
import ch.clicktotranslate.segment.infrastructure.event.TranslatedSegmentBundleEventMapper;
import ch.clicktotranslate.segment.infrastructure.web.SegmentDtoMapper;
import ch.clicktotranslate.translation.infrastructure.TextTranslationBridgeController;

@Configuration
public class SegmentConfiguration {

	@Bean
	public TextTranslator segmentTranslator(TextTranslationBridgeController textTranslationBridgeController) {
		return new TextTranslatorBridge(textTranslationBridgeController);
	}

	@Bean
	public SegmentTranslation translateWord(TextTranslator textTranslator) {
		return new SegmentTranslation(textTranslator);
	}

	@Bean
	public SegmentBundleTranslationController translateWordController(SegmentTranslation segmentTranslation,
			EventPublisher eventPublisher, TranslatedWordEventMapper eventFactory,
			SegmentBundleMapper segmentBundleMapper) {
		return new SegmentBundleTranslationController(segmentTranslation, eventPublisher, eventFactory,
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
	public TranslatedSegmentBundleEventMapper translatedWordEventMapper() {
		return new TranslatedSegmentBundleEventMapper();
	}

	@Bean
	public TranslatedWordEventMapper translatedWordEventFactory() {
		return new TranslatedWordEventMapper();
	}

	@Bean
	public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher,
			TranslatedSegmentBundleEventMapper eventMapper) {
		return new SpringEventPublisher(applicationEventPublisher, eventMapper);
	}

}
