package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.Term;
import ch.clicktotranslate.vocabulary.domain.TextSpan;
import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.UserId;
import ch.clicktotranslate.vocabulary.domain.Entry;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

class VocabularyJpaMapper {

	JpaEntryEntity toJpaEntryEntity(Entry entry) {
		JpaEntryEntity entity = new JpaEntryEntity();
		entity.setId(entry.id() == null ? null : entry.id().value());
		entity.setUserId(entry.userId().value());
		entity.setLanguage(entry.term().language().name());
		entity.setTerm(entry.term().term());
		entity.setTermCustomization(entry.termCustomization().orElse(null));
		entity.setTranslations(toJpaTranslations(entry.translations()));

		for (Usage usage : entry.usages()) {
			JpaUsageEntity usageEntity = toJpaUsageEntity(usage);
			entity.addUsage(usageEntity);
		}
		return entity;
	}

	JpaUsageEntity toJpaUsageEntity(Usage usage) {
		JpaUsageEntity usageEntity = new JpaUsageEntity();
		usageEntity.setId(usage.id() == null ? null : usage.id().value());
		usageEntity.setSentence(usage.sentence());
		usageEntity.setSentenceStart(usage.sentenceSpan().start());
		usageEntity.setSentenceEnd(usage.sentenceSpan().end());
		usageEntity.setTranslation(usage.translation());
		usageEntity.setTranslationStart(usage.translationSpan().start());
		usageEntity.setTranslationEnd(usage.translationSpan().end());
		usageEntity.setTargetLanguage(usage.targetLanguage().name());
		return usageEntity;
	}

	Entry toDomainEntry(JpaEntryEntity entity) {
		return new Entry(Entry.Id.of(entity.getId()), UserId.of(entity.getUserId()),
				new Term(Language.valueOf(entity.getLanguage()), entity.getTerm()),
				entity.getTermCustomization(), toDomainTranslations(entity.getTranslations()),
				toDomainUsages(entity.getUsages()), entity.getLastEdit(), entity.getCreatedAt());
	}

	Usage toDomainUsage(JpaUsageEntity entity) {
		return new Usage(Usage.Id.of(entity.getId()), entity.getSentence(),
				new TextSpan(entity.getSentenceStart(), entity.getSentenceEnd()), entity.getTranslation(),
				new TextSpan(entity.getTranslationStart(), entity.getTranslationEnd()),
				Language.valueOf(entity.getTargetLanguage()), entity.getLastEdit(), entity.getCreatedAt());
	}

	List<Usage> toDomainUsages(Collection<JpaUsageEntity> usages) {
		return usages.stream()
			.sorted(Comparator.comparing(JpaUsageEntity::getId, Comparator.nullsLast(Long::compareTo)))
			.map(this::toDomainUsage)
			.toList();
	}

	private Set<JpaTermTranslation> toJpaTranslations(List<Term> translations) {
		return translations.stream().map(translation -> {
			JpaTermTranslation value = new JpaTermTranslation();
			value.setLanguage(translation.language().name());
			value.setTerm(translation.term());
			return value;
		}).collect(java.util.stream.Collectors.toSet());
	}

	private List<Term> toDomainTranslations(Collection<JpaTermTranslation> translations) {
		return translations.stream().map(translation -> new Term(Language.valueOf(translation.getLanguage()),
				translation.getTerm())).toList();
	}

}


