package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.VocabularyUsageController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vocabulary")
public class VocabularyUsageRestController {

	private final VocabularyUsageController vocabularyUsageController;

	public VocabularyUsageRestController(VocabularyUsageController vocabularyUsageController) {
		this.vocabularyUsageController = vocabularyUsageController;
	}

	@DeleteMapping("/entries/{entryId}/usages/{usageId}")
	public void deleteUsage(@PathVariable Long entryId, @PathVariable Long usageId) {
		vocabularyUsageController.delete(entryId, usageId);
	}

	@PatchMapping("/entries/{entryId}/usages/{usageId}/star")
	public void starUsage(@PathVariable Long entryId, @PathVariable Long usageId) {
		vocabularyUsageController.star(entryId, usageId);
	}

}
