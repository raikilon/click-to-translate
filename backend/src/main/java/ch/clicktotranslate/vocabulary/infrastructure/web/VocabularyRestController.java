package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.VocabularyController;
import ch.clicktotranslate.vocabulary.domain.Language;
import java.util.List;
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

	public VocabularyRestController(VocabularyController vocabularyController, VocabularyDtoMapper vocabularyDtoMapper) {
		this.vocabularyController = vocabularyController;
		this.vocabularyDtoMapper = vocabularyDtoMapper;
	}

	@GetMapping
	public List<EntryDto> listAll() {
		return vocabularyDtoMapper.toEntryDto(vocabularyController.listAll());
	}

	@GetMapping("/search")
	public List<EntryDto> search(@RequestParam("q") String query) {
		return vocabularyDtoMapper.toEntryDto(vocabularyController.search(query));
	}

	@GetMapping("/{sourceLanguage}")
	public List<EntryDto> listBySourceLanguage(@PathVariable Language sourceLanguage) {
		return vocabularyDtoMapper.toEntryDto(vocabularyController.listBySourceLanguage(sourceLanguage));
	}

	@GetMapping("/entries/{entryId}/forms")
	public List<FormDto> listForms(@PathVariable Long entryId) {
		return vocabularyDtoMapper.toFormDto(vocabularyController.listForms(entryId));
	}

	@PatchMapping("/entries/{entryId}/translation")
	public void setTranslationPreference(@PathVariable Long entryId,
			@RequestBody TranslationUpdateDto request) {
		vocabularyController.setTranslationPreference(vocabularyDtoMapper.toTranslationUpdate(entryId, request));
	}

	@DeleteMapping("/entries/{entryId}")
	public void deleteEntry(@PathVariable Long entryId) {
		vocabularyController.deleteEntry(entryId);
	}

	@DeleteMapping("/{sourceLanguage}")
	public void clearBySourceLanguage(@PathVariable Language sourceLanguage) {
		vocabularyController.clearBySourceLanguage(sourceLanguage);
	}

}

