package ch.clicktotranslate.vocabulary.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record EntryItem(Long entryId, Long lexemeId, String lemma, Language sourceLanguage,
		String effectiveTranslation) {
}

