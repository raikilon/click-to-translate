package ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper;

import ch.clicktotranslate.vocabulary.domain.entity.TranslatedLemma;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationResponse;

public class TranslationResponseToTranslatedLemmaMapper {

	public TranslatedLemma map(TranslationResponse response) {
		return new TranslatedLemma();
	}

}
