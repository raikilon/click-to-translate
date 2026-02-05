package ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper;

import ch.clicktotranslate.vocabulary.domain.entity.Usage;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.entity.JpaUsageEntity;

public class UsageJpaMapper {

	public Usage toRecord(JpaUsageEntity entity) {
		Usage record = new Usage();
		return record;
	}

	public JpaUsageEntity toEntity(Usage record) {
		JpaUsageEntity entity = new JpaUsageEntity();
		return entity;
	}

}
