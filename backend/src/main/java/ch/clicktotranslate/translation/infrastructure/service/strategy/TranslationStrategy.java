package ch.clicktotranslate.translation.infrastructure.service.strategy;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.domain.entity.TranslatedWord;

public interface TranslationStrategy {

    TranslatedWord translate(TranslateWord input);

    TranslationProvider getProvider();
}
