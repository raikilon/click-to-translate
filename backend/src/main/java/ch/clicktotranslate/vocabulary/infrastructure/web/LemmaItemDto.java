package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.Language;

public record LemmaItemDto(Long lemmaId, String lemma, String lemmaTranslation, Language sourceLanguage,
		Language targetLanguage) {
}
