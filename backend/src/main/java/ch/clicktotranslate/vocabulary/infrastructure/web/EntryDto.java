package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.Language;
import java.time.Instant;
import java.util.List;

public record EntryDto(Long entryId, Language sourceLanguage, String sourceLemma, String customizationLemma,
		List<TermDto> translations, List<UsageDto> usages, Instant lastEdit, Instant createdAt) {
}
