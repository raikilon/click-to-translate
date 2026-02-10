package ch.clicktotranslate.segment.infrastructure.web;

public record TranslatedSegmentDto(String word, String sentence, String translatedWord, String translatedSentence) {
}
