package ch.clicktotranslate.translation.domain.entity;

import lombok.Getter;

@Getter
public class TranslationRequest {
	private String text;
	private String sourceLanguage;
	private String targetLanguage;

	public TranslationRequest() {
	}

	public TranslationRequest(String text, String sourceLanguage, String targetLanguage) {
		this.text = text;
		this.sourceLanguage = sourceLanguage;
		this.targetLanguage = targetLanguage;
	}

}
