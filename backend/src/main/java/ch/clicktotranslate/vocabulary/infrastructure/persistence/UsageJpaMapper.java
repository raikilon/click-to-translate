package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import ch.clicktotranslate.vocabulary.domain.entity.Usage;

public class UsageJpaMapper {

	public Usage toRecord(JpaUsageEntity entity) {
		return new Usage(entity.getId(), entity.getLemma().getId(), entity.getWord(), entity.getWordTranslation(),
				entity.getUsage(), entity.getUsageTranslation());
	}

	public JpaUsageEntity toEntity(Usage record, JpaLemmaEntity lemmaEntity) {
		JpaUsageEntity entity = new JpaUsageEntity();
		entity.setId(record.id());
		entity.setLemma(lemmaEntity);
		entity.setWord(record.word());
		entity.setWordTranslation(record.wordTranslation());
		entity.setUsage(record.usage());
		entity.setUsageTranslation(record.usageTranslation());
		return entity;
	}

}
