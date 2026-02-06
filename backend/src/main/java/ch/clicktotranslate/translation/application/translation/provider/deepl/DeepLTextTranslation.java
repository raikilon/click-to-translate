package ch.clicktotranslate.translation.application.translation.provider.deepl;

public interface DeepLTextTranslation {

	String translate(String text, String sourceLanguage, String targetLanguage);

}
