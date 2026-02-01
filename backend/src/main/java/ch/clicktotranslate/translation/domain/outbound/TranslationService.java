package ch.clicktotranslate.translation.domain.outbound;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.domain.entity.TranslatedWord;

public interface TranslationService {
	TranslatedWord translate(TranslateWord input);
}
