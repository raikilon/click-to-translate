package ch.clicktotranslate.translation.domain.entity;

public record TranslateWord(
		String userId,
		String word,
		String sentence,
		String sourceLanguage,
		String targetLanguage
) {
}
