package ch.clicktotranslate.lemmatizer.application;

public class SegmentBundleCreatedController {

	private final WordLemmatizer wordLemmatizer;

	public SegmentBundleCreatedController(WordLemmatizer wordLemmatizer) {
		this.wordLemmatizer = wordLemmatizer;
	}

	public void lemmatize(TranslatedSegmentBundle segmentBundle) {
		wordLemmatizer.lemmatize(segmentBundle);
	}

}
