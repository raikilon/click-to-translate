package ch.clicktotranslate.translation.domain.entity;

public record TranslatedWord(
		String word,
		String sentence,
		String wordTranslation,
		String sentenceTranslation
) {
}
