package ch.clicktotranslate.translation.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.clicktotranslate.translation.domain.Language;

public record SegmentBundleDto(String userId, String word, String sentence, LanguageDto sourceLanguage,
		LanguageDto targetLanguage, SourceDto source, SourceMetadataDto sourceMetadata, Instant occurredAt) {
	public record SourceDto(String type, String id, String title) {
	}

	public enum LanguageDto {

		GERMAN("de"), ENGLISH("en"), SPANISH("es"), FRENCH("fr"), ITALIAN("it"), NORWEGIAN("nb"), DANISH("da"),
		ESTONIAN("et"), FINNISH("fi"), PORTUGUESE("pt"), SLOVENIAN("sl"), SWEDISH("sv");

		private static final Map<String, LanguageDto> BY_CODE = Stream.of(values())
			.collect(Collectors.toMap(LanguageDto::code, language -> language));

		private final String code;

		LanguageDto(String code) {
			this.code = code;
		}

		@JsonValue
		public String code() {
			return code;
		}

		@JsonCreator
		public static LanguageDto fromCode(String code) {
			if (code == null || code.isBlank()) {
				throw new IllegalArgumentException("Language code is required.");
			}

			String normalized = code.trim().toLowerCase(Locale.ENGLISH);
			LanguageDto language = BY_CODE.get(normalized);
			if (language == null) {
				throw new IllegalArgumentException("Unsupported language: " + code);
			}

			return language;
		}

		public Language toDomain() {
			return Language.valueOf(name());
		}

	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
			defaultImpl = GenericSourceMetadataDto.class)
	@JsonSubTypes({ @JsonSubTypes.Type(value = GenericSourceMetadataDto.class, name = "generic"),
			@JsonSubTypes.Type(value = YoutubeSourceMetadataDto.class, name = "youtube") })
	public interface SourceMetadataDto {

	}

	public record GenericSourceMetadataDto(String url, String domain, Integer selectionOffset,
			Integer paragraphIndex) implements SourceMetadataDto {
	}

	public record YoutubeSourceMetadataDto(String url, String domain, String videoId,
			Integer timestampSeconds) implements SourceMetadataDto {
	}
}
