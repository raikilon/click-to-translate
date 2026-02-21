package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Table(indexes = { @Index(columnList = "entry_id"), @Index(columnList = "sentence"),
		@Index(columnList = "translation") })
@Entity
@jakarta.persistence.EntityListeners(AuditingEntityListener.class)
public class JpaUsageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn
	private JpaEntryEntity entry;

	@Column(nullable = false)
	private String sentence;

	@Column(nullable = false)
	private Integer sentenceStart;

	@Column(nullable = false)
	private Integer sentenceEnd;

	@Column(nullable = false)
	private String translation;

	@Column(nullable = false)
	private Integer translationStart;

	@Column(nullable = false)
	private Integer translationEnd;

	@Column(nullable = false)
	private String targetLanguage;

	@LastModifiedDate
	@Column(nullable = false)
	private Instant lastEdit;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JpaEntryEntity getEntry() {
		return entry;
	}

	public void setEntry(JpaEntryEntity entry) {
		this.entry = entry;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getTargetLanguage() {
		return targetLanguage;
	}

	public void setTargetLanguage(String targetLanguage) {
		this.targetLanguage = targetLanguage;
	}

	public Integer getSentenceStart() {
		return sentenceStart;
	}

	public void setSentenceStart(Integer sentenceStart) {
		this.sentenceStart = sentenceStart;
	}

	public Integer getSentenceEnd() {
		return sentenceEnd;
	}

	public void setSentenceEnd(Integer sentenceEnd) {
		this.sentenceEnd = sentenceEnd;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public Integer getTranslationStart() {
		return translationStart;
	}

	public void setTranslationStart(Integer translationStart) {
		this.translationStart = translationStart;
	}

	public Integer getTranslationEnd() {
		return translationEnd;
	}

	public void setTranslationEnd(Integer translationEnd) {
		this.translationEnd = translationEnd;
	}

	public Instant getLastEdit() {
		return lastEdit;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

}


