package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.PageRequest;
import ch.clicktotranslate.vocabulary.application.VocabularyUsageController;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vocabulary")
public class VocabularyUsageRestController {

	private final VocabularyUsageController vocabularyUsageController;

	private final UsageDtoMapper usageDtoMapper;

	private final PageRequestDtoMapper pageRequestDtoMapper;

	public VocabularyUsageRestController(VocabularyUsageController vocabularyUsageController,
			UsageDtoMapper usageDtoMapper,
			PageRequestDtoMapper pageRequestDtoMapper) {
		this.vocabularyUsageController = vocabularyUsageController;
		this.usageDtoMapper = usageDtoMapper;
		this.pageRequestDtoMapper = pageRequestDtoMapper;
	}

	@GetMapping("/entries/{entryId}/usages")
	public PageEnvelope<UsageDto> listByEntry(@PathVariable Long entryId, Pageable pageable) {
		PageRequest pageRequest = pageRequestDtoMapper.toPageRequest(pageable);
		return PageEnvelope.from(vocabularyUsageController.listByEntry(entryId, pageRequest),
				usage -> usageDtoMapper.toDto(entryId, usage));
	}

	@DeleteMapping("/entries/{entryId}/usages/{usageId}")
	public void deleteUsage(@PathVariable Long entryId, @PathVariable Long usageId) {
		vocabularyUsageController.delete(entryId, usageId);
	}

}

