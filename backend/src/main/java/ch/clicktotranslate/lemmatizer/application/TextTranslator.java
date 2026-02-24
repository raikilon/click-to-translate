package ch.clicktotranslate.lemmatizer.application;

public interface TextTranslator {

	String translate(String text, String sourceLanguage, String targetLanguage, String context);

}
