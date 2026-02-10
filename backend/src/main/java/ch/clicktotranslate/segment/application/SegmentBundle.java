package ch.clicktotranslate.segment.application;

import java.time.Instant;

public record SegmentBundle(String userId, String word, String sentence, String sourceLanguage, String targetLanguage,
		Source source, SourceMetadata sourceMetadata, Instant occurredAt) {
	public record Source(String type, String id, String title) {
	}

	public interface SourceMetadata {

	}

	public record GenericSourceMetadata(String url, String domain, Integer selectionOffset,
			Integer paragraphIndex) implements SourceMetadata {
	}

	public record YoutubeSourceMetadata(String url, String domain, String videoId,
			Integer timestampSeconds) implements SourceMetadata {
	}
}
