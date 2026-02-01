package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class JpaUsageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "lemma_id")
	private JpaLemmaEntity lemma;

	private String word;

	private String wordTranslation;

	private String usage;

	private String usageTranslation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JpaLemmaEntity getLemma() {
		return lemma;
	}

	public void setLemma(JpaLemmaEntity lemma) {
		this.lemma = lemma;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getWordTranslation() {
		return wordTranslation;
	}

	public void setWordTranslation(String wordTranslation) {
		this.wordTranslation = wordTranslation;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getUsageTranslation() {
		return usageTranslation;
	}

	public void setUsageTranslation(String usageTranslation) {
		this.usageTranslation = usageTranslation;
	}

}
