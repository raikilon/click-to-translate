package ch.clicktotranslate.translation.application.web;

import org.jmolecules.ddd.annotation.ValueObject;

import ch.clicktotranslate.translation.domain.Language;

import java.time.Instant;

@ValueObject
public record SegmentBundle(String userId, String word, String sentence, Language sourceLanguage,
		Language targetLanguage, Source source, SourceMetadata sourceMetadata, Instant occurredAt) {
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
