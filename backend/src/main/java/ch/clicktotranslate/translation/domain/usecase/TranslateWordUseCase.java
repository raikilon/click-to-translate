package ch.clicktotranslate.translation.domain.usecase;

import ch.clicktotranslate.translation.domain.outbound.EventPublisher;
import ch.clicktotranslate.translation.domain.outbound.TranslationService;
import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.domain.entity.TranslatedWord;

public class TranslateWordUseCase {
	private final TranslationService translationService;
	private final EventPublisher eventPublisher;

	public TranslateWordUseCase(TranslationService translationService, EventPublisher eventPublisher) {
		this.translationService = translationService;
		this.eventPublisher = eventPublisher;
	}

	public TranslatedWord execute(TranslateWord input) {
		return null;
	}
}
