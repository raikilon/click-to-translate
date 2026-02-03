package ch.clicktotranslate.translation.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TranslateWord {
	private String userId;
	private String word;
	private String sentence;
	private String sourceLanguage;
	private String targetLanguage;

}

