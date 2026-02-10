package ch.clicktotranslate.segment.application;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record TranslatedWordEvent(String userId, String word, String sentence, String wordTranslation,
		String sentenceTranslation, String sourceLanguage, String targetLanguage, Source source,
		SourceMetadata sourceMetadata, Instant occurredAt) {
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
