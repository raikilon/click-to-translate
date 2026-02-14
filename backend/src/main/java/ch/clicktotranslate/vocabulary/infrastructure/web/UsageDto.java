package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.Language;
import java.time.Instant;

public record UsageDto(Long usageId, Long entryId, String sentence, Integer sentenceStart, Integer sentenceEnd,
		String translation, Integer translationStart, Integer translationEnd, Language targetLanguage, Instant lastEdit,
		Instant createdAt) {
}
