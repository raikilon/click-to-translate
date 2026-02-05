package ch.clicktotranslate.translation.domain;

import org.jmolecules.ddd.annotation.Service;

@Service
public class SegmentTranslation {

	private final TextTranslation textTranslation;

	public SegmentTranslation(TextTranslation textTranslation) {
		this.textTranslation = textTranslation;
	}

	public TranslatedSegment translate(Segment segment) {
		return new TranslatedSegment(segment.word(), segment.sentence(),
				translateText(segment.word(), segment.sourceLanguage(), segment.targetLanguage()),
				translateText(segment.sentence(), segment.sourceLanguage(), segment.targetLanguage()));
	}

	private String translateText(String text, String sourceLanguage, String targetLanguage) {
		if (text == null || text.isBlank()) {
			return null;
		}

		TextToTranslate request = new TextToTranslate(text, sourceLanguage, targetLanguage);

		String translatedText = textTranslation.translate(request);

		if (translatedText == null || translatedText.isBlank()) {
			throw new IllegalStateException("Translation service returned no result.");
		}

		return translatedText;
	}

}
