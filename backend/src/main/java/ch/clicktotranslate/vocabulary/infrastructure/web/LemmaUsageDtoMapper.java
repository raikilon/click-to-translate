package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageDeletion;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageItem;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageUpdate;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsagesQuery;
import java.util.List;

public class LemmaUsageDtoMapper {

	public LemmaUsageUpdate toUpdate(LemmaUsageUpdateDto request) {
		return new LemmaUsageUpdate(request.userId(), request.usageId(), request.word(), request.wordTranslation(),
				request.usage(), request.usageTranslation());
	}

	public LemmaUsageDeletion toDelete(LemmaUsageDeleteDto request) {
		return new LemmaUsageDeletion(request.userId(), request.usageId());
	}

	public LemmaUsagesQuery toQuery(LemmaUsagesQueryDto request) {
		return new LemmaUsagesQuery(request.userId(), request.lemmaId());
	}

	public List<LemmaUsageItemDto> toDto(List<LemmaUsageItem> items) {
		return items.stream()
			.map(item -> new LemmaUsageItemDto(item.usageId(), item.word(), item.wordTranslation(), item.usage(),
					item.usageTranslation()))
			.toList();
	}

}
