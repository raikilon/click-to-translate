package ch.clicktotranslate.translation.infrastructure.event;

import ch.clicktotranslate.translation.domain.entity.TranslatedWord;
import ch.clicktotranslate.translation.infrastructure.controller.model.TranslateRequest;

public class TranslatedWordEventMapper {
	public TranslatedWordEvent map(TranslateRequest request, TranslatedWord translatedWord) {
		TranslatedWordEvent event = new TranslatedWordEvent();
		event.setUserId(request.getUserId());
		event.setWord(request.getWord());
		event.setSentence(request.getSentence());
		event.setSourceLanguage(request.getSourceLanguage());
		event.setTargetLanguage(request.getTargetLanguage());
		event.setOccurredAt(request.getOccurredAt());
		event.setSource(mapSource(request.getSource()));
		event.setSourceMetadata(mapSourceMetadata(request.getSourceMetadata()));

		event.setWordTranslation(translatedWord.getWordTranslation());
		event.setSentenceTranslation(translatedWord.getSentenceTranslation());
		return event;
	}

	private TranslatedWordEvent.Source mapSource(TranslateRequest.Source source) {
		if (source == null) {
			return null;
		}

		TranslatedWordEvent.Source mapped = new TranslatedWordEvent.Source();
		mapped.setType(source.getType());
		mapped.setId(source.getId());
		mapped.setTitle(source.getTitle());
		return mapped;
	}

	private TranslatedWordEvent.SourceMetadata mapSourceMetadata(TranslateRequest.SourceMetadata sourceMetadata) {
        switch (sourceMetadata) {
            case TranslateRequest.GenericSourceMetadata generic -> {
                TranslatedWordEvent.GenericSourceMetadata mapped =
                        new TranslatedWordEvent.GenericSourceMetadata();
                mapped.setUrl(generic.getUrl());
                mapped.setDomain(generic.getDomain());
                mapped.setSelectionOffset(generic.getSelectionOffset());
                mapped.setParagraphIndex(generic.getParagraphIndex());
                return mapped;
            }
            case TranslateRequest.YoutubeSourceMetadata youtube -> {
                TranslatedWordEvent.YoutubeSourceMetadata mapped =
                        new TranslatedWordEvent.YoutubeSourceMetadata();
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

