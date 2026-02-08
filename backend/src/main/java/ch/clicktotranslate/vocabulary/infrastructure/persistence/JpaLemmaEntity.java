package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;

@Entity
public class JpaLemmaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userId;

	private String lemma;

	private String lemmaTranslation;

	private String sourceLanguage;

	private String targetLanguage;

	@OneToMany(mappedBy = "lemma", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<JpaUsageEntity> usages = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getLemmaTranslation() {
		return lemmaTranslation;
	}

	public void setLemmaTranslation(String lemmaTranslation) {
		this.lemmaTranslation = lemmaTranslation;
	}

	public String getSourceLanguage() {
		return sourceLanguage;
	}

	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}

	public String getTargetLanguage() {
		return targetLanguage;
	}

	public void setTargetLanguage(String targetLanguage) {
		this.targetLanguage = targetLanguage;
	}

	public List<JpaUsageEntity> getUsages() {
		return usages;
	}

	public void setUsages(List<JpaUsageEntity> usages) {
		this.usages = usages;
	}

}
