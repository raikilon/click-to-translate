package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.VocabularyUsageController;
import java.util.List;
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

	public VocabularyUsageRestController(VocabularyUsageController vocabularyUsageController,
			UsageDtoMapper usageDtoMapper) {
		this.vocabularyUsageController = vocabularyUsageController;
		this.usageDtoMapper = usageDtoMapper;
	}

	@GetMapping("/entries/{entryId}/usages")
	public List<UsageDto> listByEntry(@PathVariable Long entryId) {
		return usageDtoMapper.toDto(entryId, vocabularyUsageController.listByEntry(entryId));
	}

	@DeleteMapping("/entries/{entryId}/usages/{usageId}")
	public void deleteUsage(@PathVariable Long entryId, @PathVariable Long usageId) {
		vocabularyUsageController.delete(entryId, usageId);
	}

}

