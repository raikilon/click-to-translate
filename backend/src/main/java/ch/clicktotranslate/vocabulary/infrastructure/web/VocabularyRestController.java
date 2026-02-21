package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.PageRequest;
import ch.clicktotranslate.vocabulary.application.VocabularyController;
import ch.clicktotranslate.vocabulary.domain.Language;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vocabulary")
public class VocabularyRestController {

	private final VocabularyController vocabularyController;

	private final VocabularyDtoMapper vocabularyDtoMapper;

	private final PageRequestDtoMapper pageRequestDtoMapper;

	public VocabularyRestController(VocabularyController vocabularyController, VocabularyDtoMapper vocabularyDtoMapper,
			PageRequestDtoMapper pageRequestDtoMapper) {
		this.vocabularyController = vocabularyController;
		this.vocabularyDtoMapper = vocabularyDtoMapper;
		this.pageRequestDtoMapper = pageRequestDtoMapper;
	}

	@GetMapping
	public PageEnvelope<EntryDto> listAll(Pageable pageable) {
		PageRequest pageRequest = pageRequestDtoMapper.toPageRequest(pageable);
		return PageEnvelope.from(vocabularyController.listAll(pageRequest), vocabularyDtoMapper::toEntryDto);
	}

	@GetMapping("/search")
	public PageEnvelope<EntryDto> search(@RequestParam("q") String query, Pageable pageable) {
		PageRequest pageRequest = pageRequestDtoMapper.toPageRequest(pageable);
		return PageEnvelope.from(vocabularyController.search(query, pageRequest), vocabularyDtoMapper::toEntryDto);
	}

	@GetMapping("/{language}")
	public PageEnvelope<EntryDto> listByLanguage(@PathVariable Language language, Pageable pageable) {
		PageRequest pageRequest = pageRequestDtoMapper.toPageRequest(pageable);
		return PageEnvelope.from(vocabularyController.listByLanguage(language, pageRequest),
				vocabularyDtoMapper::toEntryDto);
	}

	@PatchMapping("/entries/{entryId}/translation")
	public void updateEntryTranslation(@PathVariable Long entryId, @RequestBody UpdateTranslationDto request) {
		vocabularyController.updateTranslation(vocabularyDtoMapper.toTranslationUpdate(entryId, request));
	}

	@PatchMapping("/entries/{entryId}")
	public void updateEntryTerm(@PathVariable Long entryId, @RequestBody UpdateTermDto request) {
		vocabularyController.updateTerm(vocabularyDtoMapper.toUpdateTerm(entryId, request));
	}

	@DeleteMapping("/entries/{entryId}")
	public void deleteEntry(@PathVariable Long entryId) {
		vocabularyController.deleteEntry(entryId);
	}

}
