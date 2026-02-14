package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.VocabularyUsageController;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
		return usageDtoMapper.toDto(vocabularyUsageController.listByEntry(entryId));
	}

	@PatchMapping("/usages/{usageId}")
	public void updateUsage(@PathVariable Long usageId, @RequestBody UsageUpdateDto request) {
		vocabularyUsageController.update(usageDtoMapper.toUpdate(usageId, request));
	}

	@DeleteMapping("/usages/{usageId}")
	public void deleteUsage(@PathVariable Long usageId) {
		vocabularyUsageController.delete(usageId);
	}

}

