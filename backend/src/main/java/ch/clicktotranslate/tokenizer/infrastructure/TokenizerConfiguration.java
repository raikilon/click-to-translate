package ch.clicktotranslate.tokenizer.infrastructure;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.clicktotranslate.tokenizer.application.TokenizeTranslatedSegmentBundleController;
import ch.clicktotranslate.tokenizer.application.TokenizedSegmentBundleEventPublisher;
import ch.clicktotranslate.tokenizer.domain.SimpleWordTokenizer;
import ch.clicktotranslate.tokenizer.domain.TextTranslator;
import ch.clicktotranslate.tokenizer.domain.WordTokenizer;
import ch.clicktotranslate.tokenizer.domain.Tokenizer;
import ch.clicktotranslate.translation.infrastructure.TextTranslationBridgeController;

@Configuration
public class TokenizerConfiguration {

	@Bean
	public Tokenizer tokenizer() {
		return new SimpleWordTokenizer();
	}

	@Bean
	public TokenizedSegmentBundleEventMapper tokenizedSegmentBundleEventMapper() {
		return new TokenizedSegmentBundleEventMapper();
	}

	@Bean
	public TextTranslator tokenizerTextTranslator(TextTranslationBridgeController textTranslationBridgeController) {
		return new TextTranslatorBridge(textTranslationBridgeController);
	}

	@Bean
	public TranslatedSegmentBundleEventInputMapper translatedSegmentBundleEventInputMapper() {
		return new TranslatedSegmentBundleEventInputMapper();
	}

	@Bean
	public TokenizedSegmentBundleEventPublisher tokenizerEventPublisher(
			ApplicationEventPublisher applicationEventPublisher, TokenizedSegmentBundleEventMapper eventMapper) {
		return new SpringTokenizedSegmentBundleEventPublisher(applicationEventPublisher, eventMapper);
	}

	@Bean
	public WordTokenizer tokenizedSegmentBundleTranslation(Tokenizer tokenizer,
														   TextTranslator textTranslator) {
		return new WordTokenizer(tokenizer, textTranslator);
	}

	@Bean
	public TokenizeTranslatedSegmentBundleController tokenizeTranslatedSegmentBundle(
			WordTokenizer wordTokenizer,
			TokenizedSegmentBundleEventPublisher tokenizedSegmentBundleEventPublisher) {
		return new TokenizeTranslatedSegmentBundleController(wordTokenizer,
				tokenizedSegmentBundleEventPublisher);
	}

	@Bean
	public SpringTranslatedSegmentBundleEventListener translatedSegmentBundleEventListener(
			TokenizeTranslatedSegmentBundleController tokenizeTranslatedSegmentBundleController,
			TranslatedSegmentBundleEventInputMapper inputMapper) {
		return new SpringTranslatedSegmentBundleEventListener(tokenizeTranslatedSegmentBundleController, inputMapper);
	}

}
