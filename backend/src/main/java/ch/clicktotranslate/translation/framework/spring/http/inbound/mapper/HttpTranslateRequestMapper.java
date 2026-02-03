package ch.clicktotranslate.translation.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.translation.framework.spring.http.inbound.dto.TranslateRequestDto;
import ch.clicktotranslate.translation.infrastructure.controller.model.TranslateRequest;

public class HttpTranslateRequestMapper {
	public TranslateRequest map(TranslateRequestDto request) {
		return new TranslateRequest(
				request.userId(),
				request.word(),
				request.sentence(),
				request.sourceLanguage(),
				request.targetLanguage(),
				mapSource(request.source()),
				mapSourceMetadata(request.sourceMetadata()),
				request.occurredAt()
		);
	}

	private TranslateRequest.Source mapSource(TranslateRequestDto.SourceDto source) {
		if (source == null) {
			return null;
		}
		return new TranslateRequest.Source(
				source.type(),
				source.id(),
				source.title()
		);
	}

	private TranslateRequest.SourceMetadata mapSourceMetadata(TranslateRequestDto.SourceMetadataDto sourceMetadata) {
		if (sourceMetadata == null) {
			return null;
		}

		return switch (sourceMetadata) {
			case TranslateRequestDto.GenericSourceMetadataDto generic ->
					new TranslateRequest.GenericSourceMetadata(
							generic.url(),
							generic.domain(),
							generic.selectionOffset(),
							generic.paragraphIndex()
					);
			case TranslateRequestDto.YoutubeSourceMetadataDto youtube ->
					new TranslateRequest.YoutubeSourceMetadata(
							youtube.url(),
							youtube.domain(),
							youtube.videoId(),
							youtube.timestampSeconds()
					);
			default -> null;
		};
	}
}
