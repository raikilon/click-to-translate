package ch.clicktotranslate.tokenizer.infrastructure;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.clicktotranslate.tokenizer.application.TokenizeTranslatedSegmentBundleController;
import ch.clicktotranslate.tokenizer.application.SegmentBundleTokenizedEventPublisher;
import ch.clicktotranslate.tokenizer.application.SimpleWordTokenizer;
import ch.clicktotranslate.tokenizer.application.TextTranslator;
import ch.clicktotranslate.tokenizer.application.WordTokenizer;
import ch.clicktotranslate.tokenizer.application.Tokenizer;
import ch.clicktotranslate.translation.infrastructure.TextTranslationFacade;

@Configuration
public class TokenizerConfiguration {

	@Bean
	public Tokenizer tokenizer() {
		return new SimpleWordTokenizer();
	}

	@Bean
	public TextTranslator tokenizerTextTranslator(TextTranslationFacade textTranslationFacade) {
		return new TextTranslatorClient(textTranslationFacade);
	}

	@Bean
	public SegmentBundleTranslatedEventMapper translatedSegmentBundleEventInputMapper() {
		return new SegmentBundleTranslatedEventMapper();
	}

	@Bean
	public SegmentBundleTokenizedEventPublisher tokenizerEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		return new SpringSegmentBundleTokenizedEventPublisher(applicationEventPublisher);
	}

	@Bean
	public WordTokenizer tokenizedSegmentBundleTranslation(Tokenizer tokenizer, TextTranslator textTranslator,
			SegmentBundleTokenizedEventPublisher segmentBundleTokenizedEventPublisher) {
		return new WordTokenizer(tokenizer, textTranslator, segmentBundleTokenizedEventPublisher);
	}

	@Bean
	public TokenizeTranslatedSegmentBundleController tokenizeTranslatedSegmentBundle(WordTokenizer wordTokenizer) {
		return new TokenizeTranslatedSegmentBundleController(wordTokenizer);
	}

	@Bean
	public SpringSegmentBundleCreatedEventListener translatedSegmentBundleEventListener(
			TokenizeTranslatedSegmentBundleController tokenizeTranslatedSegmentBundleController,
			SegmentBundleTranslatedEventMapper eventMapper) {
		return new SpringSegmentBundleCreatedEventListener(tokenizeTranslatedSegmentBundleController, eventMapper);
	}

}
