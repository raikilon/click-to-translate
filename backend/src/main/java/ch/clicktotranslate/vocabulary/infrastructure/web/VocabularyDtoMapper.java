package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.EntryItem;
import ch.clicktotranslate.vocabulary.domain.FormItem;
import ch.clicktotranslate.vocabulary.domain.TranslationUpdate;
import java.util.List;

public class VocabularyDtoMapper {

	public List<EntryDto> toEntryDto(List<EntryItem> entries) {
		return entries.stream()
			.map(entry -> new EntryDto(entry.entryId(), entry.lexemeId(), entry.lemma(), entry.sourceLanguage(),
					entry.effectiveTranslation()))
			.toList();
	}

	public List<FormDto> toFormDto(List<FormItem> forms) {
		return forms.stream().map(form -> new FormDto(form.formId(), form.form())).toList();
	}

	public TranslationUpdate toTranslationUpdate(Long entryId, TranslationUpdateDto dto) {
		return new TranslationUpdate(entryId, dto.targetLanguage(), dto.translation());
	}

}

