package ch.clicktotranslate.translation.infrastructure.event;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TranslatedWordEvent {
	private String userId;
	private String word;
	private String sentence;
	private String wordTranslation;
	private String sentenceTranslation;
	private String sourceLanguage;
	private String targetLanguage;
	private Source source;
	private SourceMetadata sourceMetadata;
	private java.time.Instant occurredAt;

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

