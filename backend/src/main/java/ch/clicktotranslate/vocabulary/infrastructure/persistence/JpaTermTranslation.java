package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Embeddable
public class JpaTermTranslation {

	@Column(nullable = false)
	private String language;

	@Column(nullable = false)
	private String term;

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (JpaTermTranslation) obj;
		return Objects.equals(this.language, that.language) && Objects.equals(this.term, that.term);
	}

	@Override
	public int hashCode() {
		return Objects.hash(language, term);
	}

}
