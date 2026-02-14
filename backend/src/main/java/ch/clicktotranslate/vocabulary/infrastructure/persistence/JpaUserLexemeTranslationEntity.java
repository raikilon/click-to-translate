package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user_lexeme_translation", uniqueConstraints = {
		@UniqueConstraint(name = "uk_user_lexeme_translation_unique",
				columnNames = { "user_id", "source_lexeme_id", "target_language", "gloss" }) })
public class JpaUserLexemeTranslationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "source_lexeme_id")
	private JpaLexemeEntity sourceLexeme;

	@Column(name = "target_language", nullable = false)
	private String targetLanguage;

	@Column(nullable = false)
	private String gloss;

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

	public JpaLexemeEntity getSourceLexeme() {
		return sourceLexeme;
	}

	public void setSourceLexeme(JpaLexemeEntity sourceLexeme) {
		this.sourceLexeme = sourceLexeme;
	}

	public String getTargetLanguage() {
		return targetLanguage;
	}

	public void setTargetLanguage(String targetLanguage) {
		this.targetLanguage = targetLanguage;
	}

	public String getGloss() {
		return gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

}
