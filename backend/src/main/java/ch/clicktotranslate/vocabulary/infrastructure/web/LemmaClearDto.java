package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.Language;

public record LemmaClearDto(String userId, Language sourceLanguage) {
}
