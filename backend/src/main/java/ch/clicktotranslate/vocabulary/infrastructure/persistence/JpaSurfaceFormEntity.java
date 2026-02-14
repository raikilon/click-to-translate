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
@Table(name = "surface_form", uniqueConstraints = {
		@UniqueConstraint(name = "uk_surface_form_lexeme_form", columnNames = { "lexeme_id", "form" }) })
public class JpaSurfaceFormEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "lexeme_id")
	private JpaLexemeEntity lexeme;

	@Column(nullable = false)
	private String form;

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

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

}
