package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.UsageItem;
import ch.clicktotranslate.vocabulary.domain.UsageUpdate;
import java.util.List;

public class UsageDtoMapper {

	public List<UsageDto> toDto(List<UsageItem> usages) {
		return usages.stream()
			.map(usage -> new UsageDto(usage.usageId(), usage.entryId(), usage.surfaceFormId(), usage.sentence(),
					usage.targetLanguage(), usage.translatedSentence(), usage.sourceStart(), usage.sourceEnd(),
					usage.translatedStart(), usage.translatedEnd()))
			.toList();
	}

	public UsageUpdate toUpdate(Long usageId, UsageUpdateDto dto) {
		return new UsageUpdate(usageId, dto.sentence(), dto.targetLanguage(), dto.translatedSentence(),
				dto.sourceStart(), dto.sourceEnd(), dto.translatedStart(), dto.translatedEnd());
	}

}

