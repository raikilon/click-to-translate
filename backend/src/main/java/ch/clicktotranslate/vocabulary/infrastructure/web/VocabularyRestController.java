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

	public VocabularyRestController(VocabularyController vocabularyController,
			VocabularyDtoMapper vocabularyDtoMapper) {
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

	@GetMapping("/{language}")
	public List<EntryDto> listByLanguage(@PathVariable Language language) {
		return vocabularyDtoMapper.toEntryDto(vocabularyController.listByLanguage(language));
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

