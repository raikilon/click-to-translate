package ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper;

import ch.clicktotranslate.vocabulary.framework.spring.persistence.JpaLemmaEntity;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.model.LemmaRecord;

public class LemmaJpaMapper {
	public LemmaRecord toRecord(JpaLemmaEntity entity) {
		LemmaRecord record = new LemmaRecord();
		record.setId(entity.getId());
		return record;
	}

	public JpaLemmaEntity toEntity(LemmaRecord record) {
		JpaLemmaEntity entity = new JpaLemmaEntity();
		entity.setId(record.getId());
		return entity;
	}
}
