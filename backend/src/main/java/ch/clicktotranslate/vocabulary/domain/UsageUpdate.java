package ch.clicktotranslate.vocabulary.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record UsageUpdate(Long usageId, String sentence, Language targetLanguage, String translatedSentence,
		Integer sourceStart, Integer sourceEnd, Integer translatedStart, Integer translatedEnd) {

	public UsageUpdate {
		if (usageId == null || usageId <= 0) {
			throw new IllegalArgumentException("usageId must be > 0");
		}
		if (sentence == null || sentence.isBlank()) {
			throw new IllegalArgumentException("sentence must not be blank");
		}
		if (targetLanguage == null) {
			throw new IllegalArgumentException("targetLanguage must not be null");
		}
		if (translatedSentence == null || translatedSentence.isBlank()) {
			throw new IllegalArgumentException("translatedSentence must not be blank");
		}
		if (sourceStart == null || sourceEnd == null || translatedStart == null || translatedEnd == null) {
			throw new IllegalArgumentException("offsets must not be null");
		}
	}

}

