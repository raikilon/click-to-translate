package ch.clicktotranslate.vocabulary.infrastructure.web;

public record LemmaUpdateDto(String userId, Long lemmaId, String lemma, String lemmaTranslation) {
}
