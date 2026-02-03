package ch.clicktotranslate.translation.framework.spring.http.inbound.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class TranslateRequestDto {
    private String userId;

    private String wordSurface;

    private String sentence;

    private String sourceLanguage;

    private String targetLanguage;

    private SourceDto source;

    private SourceMetadataDto sourceMetadata;

    private Instant occurredAt;

    @Setter
    @Getter
    public static class SourceDto {
        private String type;
        private String id;
        private String title;
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type",
            defaultImpl = GenericSourceMetadataDto.class)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = GenericSourceMetadataDto.class, name = "generic"),
            @JsonSubTypes.Type(value = YoutubeSourceMetadataDto.class, name = "youtube")
    })
    public interface SourceMetadataDto {
    }

    @Setter
    @Getter
    public static class GenericSourceMetadataDto implements SourceMetadataDto {
        private String url;
        private String domain;

        private Integer selectionOffset;

        private Integer paragraphIndex;
    }

    @Setter
    @Getter
    public static class YoutubeSourceMetadataDto implements SourceMetadataDto {
        private String url;
        private String domain;

        private String videoId;

        private Integer timestampSeconds;
    }
}
