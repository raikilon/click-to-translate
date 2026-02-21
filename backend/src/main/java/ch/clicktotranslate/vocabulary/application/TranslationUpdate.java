package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;

public record TranslationUpdate(Long entryId, Language targetLanguage, String translation) {
}
