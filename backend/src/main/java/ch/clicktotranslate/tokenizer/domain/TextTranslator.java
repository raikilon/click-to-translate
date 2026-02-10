package ch.clicktotranslate.tokenizer.domain;


public interface TextTranslator {

	String translate(String text, String sourceLanguage, String targetLanguage);

}
