package ch.clicktotranslate.vocabulary.domain;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

@Entity
public class UserLexemeTranslation {

	@Identity
	private final Long id;

	private final String userId;

	private final Long sourceLexemeId;

	private final Language targetLanguage;

	private final String gloss;

	public UserLexemeTranslation(Long id, String userId, Long sourceLexemeId, Language targetLanguage, String gloss) {
		this.id = id;
		this.userId = requireUserId(userId);
		this.sourceLexemeId = requirePositiveId(sourceLexemeId, "sourceLexemeId");
		this.targetLanguage = requireLanguage(targetLanguage);
		this.gloss = requireGloss(gloss);
	}

	public UserLexemeTranslation(String userId, Long sourceLexemeId, Language targetLanguage, String gloss) {
		this(null, userId, sourceLexemeId, targetLanguage, gloss);
	}

	public Long id() {
		return id;
	}

	public String userId() {
		return userId;
	}

	public Long sourceLexemeId() {
		return sourceLexemeId;
	}

	public Language targetLanguage() {
		return targetLanguage;
	}

	public String gloss() {
		return gloss;
	}

	private static Long requirePositiveId(Long value, String fieldName) {
		if (value == null || value <= 0) {
			throw new IllegalArgumentException(fieldName + " must be > 0");
		}
		return value;
	}

	private static Language requireLanguage(Language value) {
		if (value == null) {
			throw new IllegalArgumentException("targetLanguage must not be null");
		}
		return value;
	}

	private static String requireUserId(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("userId must not be blank");
		}
		return value.trim();
	}

	private static String requireGloss(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("gloss must not be blank");
		}
		return value.trim();
	}

}
