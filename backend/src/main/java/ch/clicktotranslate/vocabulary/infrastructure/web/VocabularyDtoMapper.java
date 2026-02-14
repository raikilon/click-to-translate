package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.UpdateTerm;
import ch.clicktotranslate.vocabulary.application.TranslationUpdate;
import ch.clicktotranslate.vocabulary.application.EntryData;
import java.util.List;

public class VocabularyDtoMapper {

	private final UsageDtoMapper usageDtoMapper = new UsageDtoMapper();

	public List<EntryDto> toEntryDto(List<EntryData> entries) {
		return entries.stream()
			.map(entry -> new EntryDto(entry.entryId(), entry.term().language(), entry.term().term(),
					entry.termCustomization().orElse(null),
					entry.translations().stream()
						.map(translation -> new TermDto(translation.language(), translation.term()))
						.toList(),
					entry.lastUsage() == null
							? null
							: usageDtoMapper.toDto(entry.entryId(), List.of(entry.lastUsage())).getFirst(),
					entry.lastEdit(), entry.createdAt()))
			.toList();
	}

	public TranslationUpdate toTranslationUpdate(Long entryId, UpdateTranslationDto dto) {
		return new TranslationUpdate(entryId, dto.language(), dto.translation());
	}

	public UpdateTerm toUpdateTerm(Long entryId, UpdateTermDto dto) {
		return new UpdateTerm(entryId, dto.lemma());
	}

}

