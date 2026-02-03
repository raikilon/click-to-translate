package ch.clicktotranslate.translation.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TranslatedWord {
	private String word;
	private String sentence;
	private String wordTranslation;
	private String sentenceTranslation;

}

