package ch.clicktotranslate.translation.domain.usecase;

import ch.clicktotranslate.translation.domain.outbound.EventPublisher;
import ch.clicktotranslate.translation.domain.outbound.TranslationService;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordInput;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordOutput;

public class TranslateWord {

    private final TranslationService translationService;
    private final EventPublisher eventPublisher;

    public TranslateWord(TranslationService translationService, EventPublisher eventPublisher) {
        this.translationService = translationService;
        this.eventPublisher = eventPublisher;
    }

    public TranslateWordOutput execute(TranslateWordInput input) {
        return null;
    }
}
