package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Term;
import ch.clicktotranslate.vocabulary.domain.Usage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public record EntryData(Long entryId, Term term, Optional<String> termCustomization,
		List<Term> translations, Usage lastUsage, Instant lastEdit, Instant createdAt) {
}

