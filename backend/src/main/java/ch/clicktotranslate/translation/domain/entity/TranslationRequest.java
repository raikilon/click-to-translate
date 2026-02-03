package ch.clicktotranslate.translation.domain.entity;

public record TranslationRequest(
		String text,
		String sourceLanguage,
		String targetLanguage
) {
}
