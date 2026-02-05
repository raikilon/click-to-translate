package ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.dto.TranslationRequest;

public class LemmaToTranslationRequestMapper {

	public TranslationRequest map(Lemma lemma) {
		return new TranslationRequest();
	}

}
