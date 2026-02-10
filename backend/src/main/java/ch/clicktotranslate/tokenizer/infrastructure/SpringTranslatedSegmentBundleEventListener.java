package ch.clicktotranslate.tokenizer.infrastructure;

import org.jmolecules.event.annotation.DomainEventHandler;
import org.springframework.context.event.EventListener;

import ch.clicktotranslate.tokenizer.application.TokenizeTranslatedSegmentBundleController;
import ch.clicktotranslate.tokenizer.application.TokenizeTranslatedSegmentBundle;
import ch.clicktotranslate.segment.infrastructure.event.TranslatedSegmentBundleEventDto;

public class SpringTranslatedSegmentBundleEventListener {

	private final TokenizeTranslatedSegmentBundleController tokenizeTranslatedSegmentBundleController;

	private final TranslatedSegmentBundleEventInputMapper inputMapper;

	public SpringTranslatedSegmentBundleEventListener(TokenizeTranslatedSegmentBundleController tokenizeTranslatedSegmentBundleController,
													  TranslatedSegmentBundleEventInputMapper inputMapper) {
		this.tokenizeTranslatedSegmentBundleController = tokenizeTranslatedSegmentBundleController;
		this.inputMapper = inputMapper;
	}

	@EventListener
	@DomainEventHandler
	public void onTranslatedSegmentBundle(TranslatedSegmentBundleEventDto eventDto) {
		TokenizeTranslatedSegmentBundle input = inputMapper.map(eventDto);
		tokenizeTranslatedSegmentBundleController.tokenize(input);
	}

}
