package ch.clicktotranslate.translation.framework.spring.http.inbound.dto;

public record TranslateResponseDto(
        String word,
        String sentence,
        String wordTranslation,
        String sentenceTranslation
) {
}
