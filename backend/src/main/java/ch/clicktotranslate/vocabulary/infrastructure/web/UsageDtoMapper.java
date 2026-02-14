package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.Usage;

import java.util.List;

public class UsageDtoMapper {

	public List<UsageDto> toDto(Long entryId, List<Usage> usages) {
		return usages.stream()
			.map(usage -> new UsageDto(usage.id().value(), entryId, usage.sentence(), usage.sentenceSpan().start(),
					usage.sentenceSpan().end(), usage.translation(), usage.translationSpan().start(),
					usage.translationSpan().end(), usage.targetLanguage(), usage.lastEdit(), usage.createdAt()))
			.toList();
	}

}
