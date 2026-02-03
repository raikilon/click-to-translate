package ch.clicktotranslate.translation.framework.spring.http.inbound.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;

public record TranslateRequestDto(
        String userId,
        String word,
        String sentence,
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
