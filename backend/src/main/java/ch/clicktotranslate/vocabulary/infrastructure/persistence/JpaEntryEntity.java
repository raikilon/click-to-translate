package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.time.Instant;
import java.util.Set;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
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
	@Column(nullable = false)
	private Instant lastEdit;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<JpaTermTranslation> translations = new HashSet<>();

	@OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<JpaUsageEntity> usages = new HashSet<>();

	public void setId(Long id) {
		this.id = id;
	}

	public void setLanguage(String sourceLanguage) {
		this.language = sourceLanguage;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public void setTermCustomization(String customizationTerm) {
		this.termCustomization = customizationTerm;
	}

	public void setTranslations(Set<JpaTermTranslation> translations) {
		this.translations = translations;
	}

	public void addUsage(JpaUsageEntity usage) {
		usage.setEntry(this);
		usages.add(usage);
	}

}
