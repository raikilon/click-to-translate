package ch.clicktotranslate.vocabulary.domain;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

@Entity
public class Usage {

	@Identity
	private final Long id;

	private final Long userLexemeId;

	private final Long surfaceFormId;

	private final String sentence;

	private final Language targetLanguage;

	private final String translatedSentence;

	private final TextSpan sourceSpan;

	private final TextSpan translatedSpan;

	private final String translatedToken;

	public Usage(Long id, Long userLexemeId, Long surfaceFormId, String sentence, Language targetLanguage,
			String translatedSentence, TextSpan sourceSpan, TextSpan translatedSpan, String translatedToken) {
		this.id = id;
		this.userLexemeId = requirePositiveId(userLexemeId, "userLexemeId");
		this.surfaceFormId = requirePositiveId(surfaceFormId, "surfaceFormId");
		this.sentence = requireText(sentence, "sentence");
		this.targetLanguage = requireLanguage(targetLanguage);
		this.translatedSentence = requireText(translatedSentence, "translatedSentence");
		this.sourceSpan = requireSpan(sourceSpan, this.sentence, "source");
		this.translatedSpan = requireSpan(translatedSpan, this.translatedSentence, "translated");
		this.translatedToken = requireText(translatedToken, "translatedToken");
	}

	public Usage(Long userLexemeId, Long surfaceFormId, String sentence, Language targetLanguage,
			String translatedSentence, TextSpan sourceSpan, TextSpan translatedSpan, String translatedToken) {
		this(null, userLexemeId, surfaceFormId, sentence, targetLanguage, translatedSentence, sourceSpan, translatedSpan,
				translatedToken);
	}

	public Long id() {
		return id;
	}

	public Long userLexemeId() {
		return userLexemeId;
	}

	public Long surfaceFormId() {
		return surfaceFormId;
	}

	public String sentence() {
		return sentence;
	}

	public Language targetLanguage() {
		return targetLanguage;
	}

	public String translatedSentence() {
		return translatedSentence;
	}

	public TextSpan sourceSpan() {
		return sourceSpan;
	}

	public TextSpan translatedSpan() {
		return translatedSpan;
	}

	public String translatedToken() {
		return translatedToken;
	}

	public Integer sourceStart() {
		return sourceSpan.start();
	}

	public Integer sourceEnd() {
		return sourceSpan.end();
	}

	public Integer translatedStart() {
		return translatedSpan.start();
	}

	public Integer translatedEnd() {
		return translatedSpan.end();
	}

	private static Long requirePositiveId(Long value, String fieldName) {
		if (value == null || value <= 0) {
			throw new IllegalArgumentException(fieldName + " must be > 0");
		}
		return value;
	}

	private static String requireText(String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(fieldName + " must not be blank");
		}
		return value.trim();
	}

	private static Language requireLanguage(Language value) {
		if (value == null) {
			throw new IllegalArgumentException("targetLanguage must not be null");
		}
		return value;
	}

	private static TextSpan requireSpan(TextSpan span, String text, String fieldName) {
		if (span == null) {
			throw new IllegalArgumentException(fieldName + " span must not be null");
		}
		if (span.end() > text.length()) {
			throw new IllegalArgumentException(fieldName + " span out of bounds");
		}
		return span;
	}

}
