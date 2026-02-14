package ch.clicktotranslate.vocabulary.domain;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

@Entity
public class SurfaceForm {

	@Identity
	private final Long id;

	private final Long lexemeId;

	private final String form;

	public SurfaceForm(Long id, Long lexemeId, String form) {
		this.id = id;
		this.lexemeId = requirePositiveId(lexemeId, "lexemeId");
		this.form = requireForm(form);
	}

	public SurfaceForm(Long lexemeId, String form) {
		this(null, lexemeId, form);
	}

	public Long id() {
		return id;
	}

	public Long lexemeId() {
		return lexemeId;
	}

	public String form() {
		return form;
	}

	private static Long requirePositiveId(Long value, String fieldName) {
		if (value == null || value <= 0) {
			throw new IllegalArgumentException(fieldName + " must be > 0");
		}
		return value;
	}

	private static String requireForm(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("form must not be blank");
		}
		return value.trim();
	}

}
