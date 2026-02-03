package ch.clicktotranslate.translation.framework.spring.events.outbound.mapper;

import ch.clicktotranslate.translation.framework.spring.events.outbound.dto.TranslatedWordEventDto;
import ch.clicktotranslate.translation.infrastructure.event.TranslatedWordEvent;

public class TranslatedWordEventMapper {
	public TranslatedWordEventDto map(TranslatedWordEvent event) {
		TranslatedWordEventDto mapped = new TranslatedWordEventDto();
		mapped.setUserId(event.getUserId());
		mapped.setWord(event.getWord());
		mapped.setSentence(event.getSentence());
		mapped.setSourceLanguage(event.getSourceLanguage());
		mapped.setTargetLanguage(event.getTargetLanguage());
		mapped.setOccurredAt(event.getOccurredAt());
		mapped.setSource(mapSource(event.getSource()));
		mapped.setSourceMetadata(mapSourceMetadata(event.getSourceMetadata()));

		mapped.setWordTranslation(event.getWordTranslation());
		mapped.setSentenceTranslation(event.getSentenceTranslation());
		return mapped;
	}

	private TranslatedWordEventDto.SourceDto mapSource(TranslatedWordEvent.Source source) {
		if (source == null) {
			return null;
		}

		TranslatedWordEventDto.SourceDto mapped = new TranslatedWordEventDto.SourceDto();
		mapped.setType(source.getType());
		mapped.setId(source.getId());
		mapped.setTitle(source.getTitle());
		return mapped;
	}

	private TranslatedWordEventDto.SourceMetadataDto mapSourceMetadata(TranslatedWordEvent.SourceMetadata sourceMetadata) {
        switch (sourceMetadata) {
            case TranslatedWordEvent.GenericSourceMetadata generic -> {
                TranslatedWordEventDto.GenericSourceMetadataDto mapped =
                        new TranslatedWordEventDto.GenericSourceMetadataDto();
                mapped.setUrl(generic.getUrl());
                mapped.setDomain(generic.getDomain());
                mapped.setSelectionOffset(generic.getSelectionOffset());
                mapped.setParagraphIndex(generic.getParagraphIndex());
                return mapped;
            }
            case TranslatedWordEvent.YoutubeSourceMetadata youtube -> {
                TranslatedWordEventDto.YoutubeSourceMetadataDto mapped =
                        new TranslatedWordEventDto.YoutubeSourceMetadataDto();
                mapped.setUrl(youtube.getUrl());
                mapped.setDomain(youtube.getDomain());
                mapped.setVideoId(youtube.getVideoId());
                mapped.setTimestampSeconds(youtube.getTimestampSeconds());
                return mapped;
            }
            default -> {
				return null;
            }
        }
	}
}

