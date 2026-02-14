package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "userId", "sourceLanguage", "sourceLemma" }) })
public class JpaEntryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userId;

	@Column(nullable = false)
	private String sourceLanguage;

	@Column(nullable = false)
	private String sourceLemma;

	private String customizationLemma;

	@Column(nullable = false)
	private Instant lastEdit;

	@Column(nullable = false)
	private Instant createdAt;

	@ElementCollection
	private List<JpaTermTranslationValue> translations = new ArrayList<>();

	@OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JpaUsageEntity> usages = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSourceLanguage() {
		return sourceLanguage;
	}

	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSourceLemma() {
		return sourceLemma;
	}

	public void setSourceLemma(String sourceLemma) {
		this.sourceLemma = sourceLemma;
	}

	public String getCustomizationLemma() {
		return customizationLemma;
	}

	public void setCustomizationLemma(String customizationLemma) {
		this.customizationLemma = customizationLemma;
	}

	public Instant getLastEdit() {
		return lastEdit;
	}

	public void setLastEdit(Instant lastEdit) {
		this.lastEdit = lastEdit;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public List<JpaTermTranslationValue> getTranslations() {
		return translations;
	}

	public void setTranslations(List<JpaTermTranslationValue> translations) {
		this.translations = translations;
	}

	public List<JpaUsageEntity> getUsages() {
		return usages;
	}

	public void addUsage(JpaUsageEntity usage) {
		usage.setEntry(this);
		usages.add(usage);
	}

}


