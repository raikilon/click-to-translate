package ch.clicktotranslate.translation.application;

public interface DeepLTextTranslation {
	String translate(String text, String sourceLanguage, String targetLanguage, String context);

}
