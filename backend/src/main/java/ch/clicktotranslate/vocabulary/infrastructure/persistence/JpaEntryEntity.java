package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.HashSet;
import java.util.List;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "userId", "sourceLanguage", "sourceLemma" }) })
public class JpaEntryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userId;

	@Column(nullable = false)
	private String language;

	@Column(nullable = false)
	private String term;

	private String termCustomization;

	@LastModifiedDate
	private Instant lastEdit;

	@CreatedDate
	private Instant createdAt;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<JpaTermTranslation> translations = new HashSet<>();

	@OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<JpaUsageEntity> usages = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String sourceLanguage) {
		this.language = sourceLanguage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String sourceLemma) {
		this.term = sourceLemma;
	}

	public String getTermCustomization() {
		return termCustomization;
	}

	public void setTermCustomization(String customizationLemma) {
		this.termCustomization = customizationLemma;
	}

	public Instant getLastEdit() {
		return lastEdit;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Set<JpaTermTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<JpaTermTranslation> translations) {
		this.translations = translations;
	}

	public Set<JpaUsageEntity> getUsages() {
		return usages;
	}

	public void addUsage(JpaUsageEntity usage) {
		usage.setEntry(this);
		usages.add(usage);
	}

}


