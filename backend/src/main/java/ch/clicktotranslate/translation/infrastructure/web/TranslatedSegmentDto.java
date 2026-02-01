package ch.clicktotranslate.translation.infrastructure.web;

public record TranslatedSegmentDto(String word, String sentence, String translatedWord, String translatedSentence) {
}
