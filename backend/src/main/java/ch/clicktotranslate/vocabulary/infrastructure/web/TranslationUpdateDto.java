package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.Language;

public record TranslationUpdateDto(Language targetLanguage, String translation) {
}

