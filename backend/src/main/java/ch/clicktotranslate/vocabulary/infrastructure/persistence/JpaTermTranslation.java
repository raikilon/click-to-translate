package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.Embeddable;

@Embeddable
public class JpaTermTranslation {

	private String language;

	private String term;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String lemma) {
		this.term = lemma;
	}

}
