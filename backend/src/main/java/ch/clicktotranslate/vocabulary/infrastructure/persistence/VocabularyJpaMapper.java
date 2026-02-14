package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import ch.clicktotranslate.vocabulary.application.EntryData;
import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.Term;
import ch.clicktotranslate.vocabulary.domain.TextSpan;
import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.UserId;
import ch.clicktotranslate.vocabulary.domain.Entry;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class VocabularyJpaMapper {

	JpaEntryEntity toJpaEntryEntity(Entry entry) {
		JpaEntryEntity entity = new JpaEntryEntity();
		entity.setId(entry.id() == null ? null : entry.id().value());
		entity.setUserId(entry.userId().value());
		entity.setSourceLanguage(entry.term().language().name());
		entity.setSourceLemma(entry.term().term());
		entity.setCustomizationLemma(entry.termCustomization().orElse(null));
		entity.setLastEdit(entry.lastEdit());
		entity.setCreatedAt(entry.createdAt());
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
		usageEntity.setLastEdit(usage.lastEdit());
		usageEntity.setCreatedAt(usage.createdAt());
		return usageEntity;
	}

	Entry toDomainEntry(JpaEntryEntity entity) {
		return new Entry(Entry.Id.of(entity.getId()), UserId.of(entity.getUserId()),
				new Term(Language.valueOf(entity.getSourceLanguage()), entity.getSourceLemma()),
				entity.getCustomizationLemma(), toDomainTranslations(entity.getTranslations()),
				toDomainUsages(entity.getUsages()), entity.getLastEdit(), entity.getCreatedAt());
	}

	Usage toDomainUsage(JpaUsageEntity entity) {
		return new Usage(Usage.Id.of(entity.getId()), entity.getSentence(),
				new TextSpan(entity.getSentenceStart(), entity.getSentenceEnd()), entity.getTranslation(),
				new TextSpan(entity.getTranslationStart(), entity.getTranslationEnd()),
				Language.valueOf(entity.getTargetLanguage()), entity.getLastEdit(), entity.getCreatedAt());
	}

	List<Usage> toDomainUsages(List<JpaUsageEntity> usages) {
		return usages.stream()
			.sorted(Comparator.comparing(JpaUsageEntity::getId, Comparator.nullsLast(Long::compareTo)))
			.map(this::toDomainUsage)
			.toList();
	}

	EntryData toEntry(JpaEntryEntity entry) {
		Usage lastUsage = entry.getUsages()
			.stream()
			.max(Comparator.comparing(JpaUsageEntity::getId, Comparator.nullsLast(Long::compareTo)))
			.map(this::toDomainUsage)
			.orElse(null);
		return new EntryData(entry.getId(),
				new Term(Language.valueOf(entry.getSourceLanguage()), entry.getSourceLemma()),
				Optional.ofNullable(entry.getCustomizationLemma()),
				toDomainTranslations(entry.getTranslations()),
				lastUsage, entry.getLastEdit(), entry.getCreatedAt());
	}

	private List<JpaTermTranslationValue> toJpaTranslations(List<Term> translations) {
		return translations.stream().map(translation -> {
			JpaTermTranslationValue value = new JpaTermTranslationValue();
			value.setLanguage(translation.language().name());
			value.setLemma(translation.term());
			return value;
		}).toList();
	}

	private List<Term> toDomainTranslations(List<JpaTermTranslationValue> translations) {
		return translations.stream().map(translation -> new Term(Language.valueOf(translation.getLanguage()),
				translation.getLemma())).toList();
	}

}


