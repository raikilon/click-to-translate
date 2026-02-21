package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.Language;
import java.time.Instant;
import java.util.List;

public record EntryDto(Long entryId, Language language, String term, String termCustomization,
		List<TermDto> translations, List<UsageDto> usages, Instant lastEdit, Instant createdAt) {
}
