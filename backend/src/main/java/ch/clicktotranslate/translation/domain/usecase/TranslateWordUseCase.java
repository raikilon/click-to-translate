package ch.clicktotranslate.translation.domain.usecase;

import ch.clicktotranslate.translation.domain.outbound.TranslationService;
import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.domain.entity.TranslationRequest;
import ch.clicktotranslate.translation.domain.entity.TranslatedWord;

public class TranslateWordUseCase {
	private final TranslationService translationService;

	public TranslateWordUseCase(TranslationService translationService) {
		this.translationService = translationService;
	}

	public TranslatedWord execute(TranslateWord input) {
		return new TranslatedWord(
				input.word(),
				input.sentence(),
				translateText(input.word(), input),
				translateText(input.sentence(), input)
		);
	}

	private String translateText(String text, TranslateWord input) {
		if (text == null || text.isBlank()) {
			return null;
		}

		TranslationRequest request = new TranslationRequest(
				text,
				input.sourceLanguage(),
				input.targetLanguage()
		);
		String translatedText = translationService.translate(request);
		if (translatedText == null || translatedText.isBlank()) {
			throw new IllegalStateException("Translation service returned no result.");
		}
		return translatedText;
	}
}
