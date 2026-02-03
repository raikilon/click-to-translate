package ch.clicktotranslate.translation.framework.spring.events.outbound.dto;

import java.time.Instant;

public record TranslatedWordEventDto(
		String userId,
		String word,
		String sentence,
		String wordTranslation,
		String sentenceTranslation,
		String sourceLanguage,
		String targetLanguage,
		SourceDto source,
		SourceMetadataDto sourceMetadata,
		Instant occurredAt
) {
	public record SourceDto(
			String type,
			String id,
			String title
	) {
	}

	public interface SourceMetadataDto {
	}

	public record GenericSourceMetadataDto(
			String url,
			String domain,
			Integer selectionOffset,
			Integer paragraphIndex
	) implements SourceMetadataDto {
	}

	public record YoutubeSourceMetadataDto(
			String url,
			String domain,
			String videoId,
			Integer timestampSeconds
	) implements SourceMetadataDto {
	}
}

