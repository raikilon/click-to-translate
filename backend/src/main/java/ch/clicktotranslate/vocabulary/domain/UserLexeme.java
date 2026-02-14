package ch.clicktotranslate.vocabulary.domain;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

@Entity
public class UserLexeme {

	@Identity
	private final Long id;

	private final Long lexemeId;

	private final String userId;

	public UserLexeme(Long id, Long lexemeId, String userId) {
		this.id = id;
		this.lexemeId = requirePositiveId(lexemeId, "lexemeId");
		this.userId = requireUserId(userId);
	}

	public UserLexeme(Long lexemeId, String userId) {
		this(null, lexemeId, userId);
	}

	public Long id() {
		return id;
	}

	public Long lexemeId() {
		return lexemeId;
	}

	public String userId() {
		return userId;
	}

	private static Long requirePositiveId(Long value, String fieldName) {
		if (value == null || value <= 0) {
			throw new IllegalArgumentException(fieldName + " must be > 0");
		}
		return value;
	}

	private static String requireUserId(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("userId must not be blank");
		}
		return value.trim();
	}

}
