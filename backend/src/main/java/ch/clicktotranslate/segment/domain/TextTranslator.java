package ch.clicktotranslate.segment.domain;

public interface TextTranslator {

	String translate(String text, String sourceLanguage, String targetLanguage);

}
