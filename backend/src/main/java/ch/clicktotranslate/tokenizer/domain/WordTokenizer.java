package ch.clicktotranslate.tokenizer.domain;

import org.jmolecules.ddd.annotation.Service;

@Service
public class WordTokenizer {

	private final Tokenizer tokenizer;

	private final TextTranslator textTranslator;

	public WordTokenizer(Tokenizer tokenizer, TextTranslator textTranslator) {
		this.tokenizer = tokenizer;
		this.textTranslator = textTranslator;
	}

	public TokenizedWord tokenize(String word, String sourceLanguage, String targetLanguage) {
		String tokenizedWord = tokenizer.tokenize(word);
		String tokenizedWordTranslation = translateToken(tokenizedWord, sourceLanguage, targetLanguage);
		return new TokenizedWord(tokenizedWord, tokenizedWordTranslation);
	}

	private String translateToken(String text, String sourceLanguage, String targetLanguage) {
		String translatedText = textTranslator.translate(text, sourceLanguage, targetLanguage);

		if (translatedText == null || translatedText.isBlank()) {
			throw new IllegalStateException("Translation service returned no result.");
		}

		return translatedText;
	}

}
