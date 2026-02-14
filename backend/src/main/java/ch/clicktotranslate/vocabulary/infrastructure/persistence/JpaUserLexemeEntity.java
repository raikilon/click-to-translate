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
@Table(name = "user_lexeme", uniqueConstraints = {
		@UniqueConstraint(name = "uk_user_lexeme_user_lexeme", columnNames = { "user_id", "lexeme_id" }) })
public class JpaUserLexemeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "lexeme_id")
	private JpaLexemeEntity lexeme;

	@Column(name = "user_id", nullable = false)
	private String userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JpaLexemeEntity getLexeme() {
		return lexeme;
	}

	public void setLexeme(JpaLexemeEntity lexeme) {
		this.lexeme = lexeme;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
