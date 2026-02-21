package ch.clicktotranslate.lemmatizer.infrastructure;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.clicktotranslate.lemmatizer.application.SegmentBundleCreatedController;
import ch.clicktotranslate.lemmatizer.application.SegmentBundleLemmatizedEventPublisher;
import ch.clicktotranslate.lemmatizer.application.SimpleWordLemmatizer;
import ch.clicktotranslate.lemmatizer.application.TextTranslator;
import ch.clicktotranslate.lemmatizer.application.WordLemmatizer;
import ch.clicktotranslate.lemmatizer.application.Lemmatizer;
import ch.clicktotranslate.translation.infrastructure.TextTranslationFacade;

@Configuration
public class LemmatizerConfiguration {

	@Bean
	public Lemmatizer lemmatizer() {
		return new SimpleWordLemmatizer();
	}

	@Bean
	public TextTranslator lemmatizerTextTranslator(TextTranslationFacade textTranslationFacade) {
		return new TextTranslatorClient(textTranslationFacade);
	}

	@Bean
	public SegmentBundleTranslatedEventMapper translatedSegmentBundleEventInputMapper() {
		return new SegmentBundleTranslatedEventMapper();
	}

	@Bean
	public SegmentBundleLemmatizedEventPublisher lemmatizerEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		return new SpringSegmentBundleLemmatizedEventPublisher(applicationEventPublisher);
	}

	@Bean
	public WordLemmatizer lemmatizedSegmentBundleTranslation(Lemmatizer lemmatizer, TextTranslator textTranslator,
			SegmentBundleLemmatizedEventPublisher segmentBundleLemmatizedEventPublisher) {
		return new WordLemmatizer(lemmatizer, textTranslator, segmentBundleLemmatizedEventPublisher);
	}

	@Bean
	public SegmentBundleCreatedController lemmatizeTranslatedSegmentBundle(WordLemmatizer wordLemmatizer) {
		return new SegmentBundleCreatedController(wordLemmatizer);
	}

	@Bean
	public SpringSegmentBundleCreatedEventListener translatedSegmentBundleEventListener(
			SegmentBundleCreatedController segmentBundleCreatedController,
			SegmentBundleTranslatedEventMapper eventMapper) {
		return new SpringSegmentBundleCreatedEventListener(segmentBundleCreatedController, eventMapper);
	}

}
