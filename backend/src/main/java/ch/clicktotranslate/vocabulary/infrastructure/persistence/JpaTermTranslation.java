package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class JpaTermTranslation {

	@Column(nullable = false)
	private String language;

	@Column(nullable = false)
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
