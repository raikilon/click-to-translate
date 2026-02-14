package ch.clicktotranslate.vocabulary.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record UsageItem(Long usageId, Long entryId, Long surfaceFormId, String sentence,
		Language targetLanguage, String translatedSentence, Integer sourceStart, Integer sourceEnd,
		Integer translatedStart, Integer translatedEnd) {
}

