package ch.clicktotranslate.tokenizer.application;

public class TokenizeTranslatedSegmentBundleController {

	private final WordTokenizer wordTokenizer;

	public TokenizeTranslatedSegmentBundleController(WordTokenizer wordTokenizer) {
		this.wordTokenizer = wordTokenizer;
	}

	public void tokenize(TranslatedSegmentBundle segmentBundle) {
		wordTokenizer.tokenize(segmentBundle);
	}

}
