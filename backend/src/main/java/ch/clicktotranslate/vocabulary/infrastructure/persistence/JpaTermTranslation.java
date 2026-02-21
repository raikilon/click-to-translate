package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class JpaTermTranslation {

	@Column(nullable = false)
	private String language;

	@Column(nullable = false)
	private String term;

}
