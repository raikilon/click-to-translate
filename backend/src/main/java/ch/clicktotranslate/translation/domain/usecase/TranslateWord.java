package ch.clicktotranslate.translation.domain.usecase;

import ch.clicktotranslate.translation.domain.outbound.EventPublisher;
import ch.clicktotranslate.translation.domain.outbound.TranslationGateway;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordInput;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordOutput;

public class TranslateWord {
	private final TranslationGateway translationGateway;
	private final EventPublisher eventPublisher;

	public TranslateWord(TranslationGateway translationGateway, EventPublisher eventPublisher) {
		this.translationGateway = translationGateway;
		this.eventPublisher = eventPublisher;
	}

	public TranslateWordOutput execute(TranslateWordInput input) {
		return null;
	}
}
