package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.UpdateTerm;
import ch.clicktotranslate.vocabulary.application.TranslationUpdate;
import ch.clicktotranslate.vocabulary.domain.Entry;
import java.util.List;

public class VocabularyDtoMapper {

	private final UsageDtoMapper usageDtoMapper = new UsageDtoMapper();

	public List<EntryDto> toEntryDto(List<Entry> entries) {
		return entries.stream().map(this::toEntryDto).toList();
	}

	public EntryDto toEntryDto(Entry entry) {
		return new EntryDto(entry.id().value(), entry.term().language(), entry.term().term(),
				entry.termCustomization().orElse(null),
				entry.translations()
					.stream()
					.map(translation -> new TermDto(translation.language(), translation.term()))
					.toList(),
				usageDtoMapper.toDto(entry.id().value(), entry.usages()), entry.lastEdit(), entry.createdAt());
	}

	public TranslationUpdate toTranslationUpdate(Long entryId, UpdateTranslationDto dto) {
		return new TranslationUpdate(entryId, dto.language(), dto.translation());
	}

	public UpdateTerm toUpdateTerm(Long entryId, UpdateTermDto dto) {
		return new UpdateTerm(entryId, dto.lemma());
	}

}
