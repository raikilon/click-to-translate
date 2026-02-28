package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.Usage;

import java.util.List;

public class UsageDtoMapper {

	public UsageDto toDto(Long entryId, Usage usage) {
		Integer translationStart = usage.translationSpan() == null ? null : usage.translationSpan().start();
		Integer translationEnd = usage.translationSpan() == null ? null : usage.translationSpan().end();
		return new UsageDto(usage.id().value(), entryId, usage.sentence(), usage.sentenceSpan().start(),
				usage.sentenceSpan().end(), usage.translation(), translationStart, translationEnd, usage.language(),
				usage.starred(), usage.lastEdit(), usage.createdAt());
	}

	public List<UsageDto> toDto(Long entryId, List<Usage> usages) {
		return usages.stream().map(usage -> toDto(entryId, usage)).toList();
	}

}
