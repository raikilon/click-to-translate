package ch.clicktotranslate.vocabulary.infrastructure.gateway.mapper;

import ch.clicktotranslate.vocabulary.infrastructure.gateway.model.UsageRecord;
import ch.clicktotranslate.vocabulary.domain.entity.Usage;

public class UsageMapper {
	public Usage toDomain(UsageRecord record) {
		return new Usage();
	}

	public UsageRecord toRecord(Usage usage) {
		return new UsageRecord();
	}
}
