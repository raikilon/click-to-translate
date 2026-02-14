package ch.clicktotranslate.vocabulary.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record TranslationUpdate(Long entryId, Language targetLanguage, String translation) {

	public TranslationUpdate {
		if (entryId == null || entryId <= 0) {
			throw new IllegalArgumentException("entryId must be > 0");
		}
		if (targetLanguage == null) {
			throw new IllegalArgumentException("targetLanguage must not be null");
		}
		if (translation == null || translation.isBlank()) {
			throw new IllegalArgumentException("translation must not be blank");
		}
	}

}

