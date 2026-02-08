package ch.clicktotranslate.vocabulary.infrastructure.web;

public record LemmaUsageItemDto(Long usageId, String word, String wordTranslation, String usage,
		String usageTranslation) {
}
