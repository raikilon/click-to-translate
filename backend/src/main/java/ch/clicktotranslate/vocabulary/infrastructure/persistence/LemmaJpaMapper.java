package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.Usage;

import java.util.List;

public class LemmaJpaMapper {

	private final UsageJpaMapper usageJpaMapper;

	public LemmaJpaMapper(UsageJpaMapper usageJpaMapper) {
		this.usageJpaMapper = usageJpaMapper;
	}

	public Lemma toRecord(JpaLemmaEntity entity) {
		List<Usage> usages = entity.getUsages().stream().map(usageJpaMapper::toRecord).toList();
		return new Lemma(entity.getId(), entity.getUserId(), entity.getLemma(), entity.getLemmaTranslation(),
				toLanguage(entity.getSourceLanguage()), toLanguage(entity.getTargetLanguage()), usages);
	}

	public JpaLemmaEntity toEntity(Lemma record) {
		JpaLemmaEntity entity = new JpaLemmaEntity();
		entity.setId(record.id());
		entity.setUserId(record.userId());
		entity.setLemma(record.lemma());
		entity.setLemmaTranslation(record.lemmaTranslation());
		entity.setSourceLanguage(toDatabaseValue(record.sourceLanguage()));
		entity.setTargetLanguage(toDatabaseValue(record.targetLanguage()));
		entity.setUsages(record.usages().stream().map(usage -> usageJpaMapper.toEntity(usage, entity)).toList());
		return entity;
	}

	private Language toLanguage(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return Language.valueOf(value);
	}

	private String toDatabaseValue(Language language) {
		if (language == null) {
			return null;
		}
		return language.name();
	}

}
