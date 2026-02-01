package ch.clicktotranslate.vocabulary.domain.entity;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record LemmaUsageUpdate(String userId, Long usageId, String word, String wordTranslation, String usage,
		String usageTranslation) {
}
