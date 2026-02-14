package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.Language;

public record EntryDto(Long entryId, Long lexemeId, String lemma, Language sourceLanguage,
		String effectiveTranslation) {
}

