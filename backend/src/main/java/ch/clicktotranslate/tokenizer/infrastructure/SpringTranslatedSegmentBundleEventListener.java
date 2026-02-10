package ch.clicktotranslate.tokenizer.infrastructure;

import org.jmolecules.event.annotation.DomainEventHandler;
import org.springframework.context.event.EventListener;

import ch.clicktotranslate.tokenizer.application.TokenizeTranslatedSegmentBundle;
import ch.clicktotranslate.tokenizer.application.TokenizeTranslatedSegmentBundleInput;
import ch.clicktotranslate.segment.infrastructure.event.TranslatedSegmentBundleEventDto;

public class SpringTranslatedSegmentBundleEventListener {

	private final TokenizeTranslatedSegmentBundle tokenizeTranslatedSegmentBundle;

	private final TranslatedSegmentBundleEventInputMapper inputMapper;

	public SpringTranslatedSegmentBundleEventListener(TokenizeTranslatedSegmentBundle tokenizeTranslatedSegmentBundle,
			TranslatedSegmentBundleEventInputMapper inputMapper) {
		this.tokenizeTranslatedSegmentBundle = tokenizeTranslatedSegmentBundle;
		this.inputMapper = inputMapper;
	}

	@EventListener
	@DomainEventHandler
	public void onTranslatedSegmentBundle(TranslatedSegmentBundleEventDto eventDto) {
		TokenizeTranslatedSegmentBundleInput input = inputMapper.map(eventDto);
		tokenizeTranslatedSegmentBundle.execute(input);
	}

}
