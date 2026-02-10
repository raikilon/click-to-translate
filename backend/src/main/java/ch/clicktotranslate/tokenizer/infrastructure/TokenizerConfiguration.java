package ch.clicktotranslate.tokenizer.infrastructure;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.clicktotranslate.tokenizer.application.TokenizeTranslatedSegmentBundle;
import ch.clicktotranslate.tokenizer.application.EventPublisher;
import ch.clicktotranslate.tokenizer.domain.SimpleWordTokenizer;
import ch.clicktotranslate.tokenizer.domain.Tokenizer;

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
	public TranslatedSegmentBundleEventInputMapper translatedSegmentBundleEventInputMapper() {
		return new TranslatedSegmentBundleEventInputMapper();
	}

	@Bean
	public EventPublisher tokenizerEventPublisher(ApplicationEventPublisher applicationEventPublisher,
			TokenizedSegmentBundleEventMapper eventMapper) {
		return new SpringEventPublisher(applicationEventPublisher, eventMapper);
	}

	@Bean
	public TokenizeTranslatedSegmentBundle tokenizeTranslatedSegmentBundle(Tokenizer tokenizer,
			EventPublisher eventPublisher) {
		return new TokenizeTranslatedSegmentBundle(tokenizer, eventPublisher);
	}

	@Bean
	public SpringTranslatedSegmentBundleEventListener translatedSegmentBundleEventListener(
			TokenizeTranslatedSegmentBundle tokenizeTranslatedSegmentBundle,
			TranslatedSegmentBundleEventInputMapper inputMapper) {
		return new SpringTranslatedSegmentBundleEventListener(tokenizeTranslatedSegmentBundle, inputMapper);
	}

}
