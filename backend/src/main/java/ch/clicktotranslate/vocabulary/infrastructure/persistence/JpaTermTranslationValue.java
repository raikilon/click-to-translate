package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.Embeddable;

@Embeddable
public class JpaTermTranslationValue {

	private String language;

	private String lemma;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

}

