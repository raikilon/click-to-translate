package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.UpdateTerm;
import ch.clicktotranslate.vocabulary.application.TranslationUpdate;
import ch.clicktotranslate.vocabulary.domain.Entry;
import ch.clicktotranslate.vocabulary.domain.Usage;
import java.util.Comparator;
import java.util.List;

public class VocabularyDtoMapper {

	private final UsageDtoMapper usageDtoMapper = new UsageDtoMapper();

	public List<EntryDto> toEntryDto(List<Entry> entries) {
		return entries.stream().map(this::toEntryDto).toList();
	}

	public EntryDto toEntryDto(Entry entry) {
		Usage lastUsage = lastUsage(entry);
		return new EntryDto(entry.id().value(), entry.term().language(), entry.term().term(),
				entry.termCustomization().orElse(null),
				entry.translations()
					.stream()
					.map(translation -> new TermDto(translation.language(), translation.term()))
					.toList(),
				lastUsage == null ? null : usageDtoMapper.toDto(entry.id().value(), lastUsage), entry.lastEdit(),
				entry.createdAt());
	}

	private Usage lastUsage(Entry entry) {
		return entry.usages()
			.stream()
			.max(Comparator.comparing(usage -> usage.id() == null ? null : usage.id().value(),
					Comparator.nullsLast(Long::compareTo)))
			.orElse(null);
	}

	public TranslationUpdate toTranslationUpdate(Long entryId, UpdateTranslationDto dto) {
		return new TranslationUpdate(entryId, dto.language(), dto.translation());
	}

	public UpdateTerm toUpdateTerm(Long entryId, UpdateTermDto dto) {
		return new UpdateTerm(entryId, dto.lemma());
	}

}
