package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
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
	private String language;

	@Column(nullable = false)
	private boolean starred;

	@LastModifiedDate
	@Column(nullable = false)
	private Instant lastEdit;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	public void setId(Long id) {
		this.id = id;
	}

	public void setEntry(JpaEntryEntity entry) {
		this.entry = entry;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setStarred(boolean starred) {
		this.starred = starred;
	}

	public void setSentenceStart(Integer sentenceStart) {
		this.sentenceStart = sentenceStart;
	}

	public void setSentenceEnd(Integer sentenceEnd) {
		this.sentenceEnd = sentenceEnd;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public void setTranslationStart(Integer translationStart) {
		this.translationStart = translationStart;
	}

	public void setTranslationEnd(Integer translationEnd) {
		this.translationEnd = translationEnd;
	}

}
