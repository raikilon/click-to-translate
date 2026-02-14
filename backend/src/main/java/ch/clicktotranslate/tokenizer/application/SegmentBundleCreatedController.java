package ch.clicktotranslate.tokenizer.application;

public class SegmentBundleCreatedController {

	private final WordTokenizer wordTokenizer;

	public SegmentBundleCreatedController(WordTokenizer wordTokenizer) {
		this.wordTokenizer = wordTokenizer;
	}

	public void tokenize(TranslatedSegmentBundle segmentBundle) {
		wordTokenizer.tokenize(segmentBundle);
	}

}
