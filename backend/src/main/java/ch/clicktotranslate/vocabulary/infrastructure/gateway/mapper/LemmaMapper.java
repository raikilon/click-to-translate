package ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.model.LemmaRecord;

public class LemmaMapper {
	public Lemma toDomain(LemmaRecord record) {
		return new Lemma();
	}

	public LemmaRecord toRecord(Lemma lemma) {
		return new LemmaRecord();
	}
}
