package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.Language;

public record UsageUpdateDto(String sentence, Language targetLanguage, String translatedSentence,
		Integer sourceStart, Integer sourceEnd, Integer translatedStart, Integer translatedEnd) {
}

