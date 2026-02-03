package ch.clicktotranslate.translation.domain.outbound;

import ch.clicktotranslate.translation.domain.entity.TranslationRequest;

public interface TranslationService {
	String translate(TranslationRequest request);
}
