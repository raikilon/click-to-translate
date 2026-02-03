package ch.clicktotranslate.translation.infrastructure.controller.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class TranslateRequest {
	private String userId;
	private String word;
	private String sentence;
	private String sourceLanguage;
	private String targetLanguage;
	private Source source;
	private SourceMetadata sourceMetadata;
	private Instant occurredAt;

    @Setter
    @Getter
    public static class Source {
		private String type;
		private String id;
		private String title;

    }

	public interface SourceMetadata {
	}

	@Setter
    @Getter
    public static class GenericSourceMetadata implements SourceMetadata {
		private String url;
		private String domain;
		private Integer selectionOffset;
		private Integer paragraphIndex;

    }

	@Setter
    @Getter
    public static class YoutubeSourceMetadata implements SourceMetadata {
		private String url;
		private String domain;
		private String videoId;
		private Integer timestampSeconds;

    }
}

