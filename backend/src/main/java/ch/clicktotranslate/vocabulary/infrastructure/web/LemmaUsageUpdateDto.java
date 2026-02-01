package ch.clicktotranslate.vocabulary.infrastructure.web;

public record LemmaUsageUpdateDto(String userId, Long usageId, String word, String wordTranslation, String usage,
		String usageTranslation) {
}
