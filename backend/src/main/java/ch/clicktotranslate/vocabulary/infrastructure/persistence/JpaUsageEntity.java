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

@Table(name = "vocabulary_usage",
		indexes = { @Index(name = "idx_vocabulary_usage_user_lexeme", columnList = "user_lexeme_id"),
				@Index(name = "idx_vocabulary_usage_surface_form", columnList = "surface_form_id"),
				@Index(name = "idx_vocabulary_usage_translated_token", columnList = "translated_token") })
@Entity
public class JpaUsageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_lexeme_id")
	private JpaUserLexemeEntity userLexeme;

	@ManyToOne(optional = false)
	@JoinColumn(name = "surface_form_id")
	private JpaSurfaceFormEntity surfaceForm;

	@Column(nullable = false)
	private String sentence;

	@Column(nullable = false)
	private String targetLanguage;

	@Column(nullable = false)
	private String translatedSentence;

	@Column(nullable = false)
	private Integer sourceStart;

	@Column(nullable = false)
	private Integer sourceEnd;

	@Column(nullable = false)
	private Integer translatedStart;

	@Column(nullable = false)
	private Integer translatedEnd;

	@Column(name = "translated_token", nullable = false)
	private String translatedToken;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JpaUserLexemeEntity getUserLexeme() {
		return userLexeme;
	}

	public void setUserLexeme(JpaUserLexemeEntity userLexeme) {
		this.userLexeme = userLexeme;
	}

	public JpaSurfaceFormEntity getSurfaceForm() {
		return surfaceForm;
	}

	public void setSurfaceForm(JpaSurfaceFormEntity surfaceForm) {
		this.surfaceForm = surfaceForm;
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

	public String getTranslatedSentence() {
		return translatedSentence;
	}

	public void setTranslatedSentence(String translatedSentence) {
		this.translatedSentence = translatedSentence;
	}

	public Integer getSourceStart() {
		return sourceStart;
	}

	public void setSourceStart(Integer sourceStart) {
		this.sourceStart = sourceStart;
	}

	public Integer getSourceEnd() {
		return sourceEnd;
	}

	public void setSourceEnd(Integer sourceEnd) {
		this.sourceEnd = sourceEnd;
	}

	public Integer getTranslatedStart() {
		return translatedStart;
	}

	public void setTranslatedStart(Integer translatedStart) {
		this.translatedStart = translatedStart;
	}

	public Integer getTranslatedEnd() {
		return translatedEnd;
	}

	public void setTranslatedEnd(Integer translatedEnd) {
		this.translatedEnd = translatedEnd;
	}

	public String getTranslatedToken() {
		return translatedToken;
	}

	public void setTranslatedToken(String translatedToken) {
		this.translatedToken = translatedToken;
	}

}
