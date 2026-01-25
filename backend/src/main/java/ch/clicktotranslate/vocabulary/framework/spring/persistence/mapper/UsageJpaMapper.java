package ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper;

import ch.clicktotranslate.vocabulary.framework.spring.persistence.JpaUsageEntity;
import ch.clicktotranslate.vocabulary.infrastructure.gateway.model.UsageRecord;

public class UsageJpaMapper {
	public UsageRecord toRecord(JpaUsageEntity entity) {
		UsageRecord record = new UsageRecord();
		record.setId(entity.getId());
		return record;
	}

	public JpaUsageEntity toEntity(UsageRecord record) {
		JpaUsageEntity entity = new JpaUsageEntity();
		entity.setId(record.getId());
		return entity;
	}
}
