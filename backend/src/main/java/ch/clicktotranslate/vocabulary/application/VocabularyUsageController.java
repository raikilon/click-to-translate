package ch.clicktotranslate.vocabulary.application;

public class VocabularyUsageController {

	private final DeleteUsage deleteUsage;

	private final StarUsage starUsage;

	public VocabularyUsageController(DeleteUsage deleteUsage, StarUsage starUsage) {
		this.deleteUsage = deleteUsage;
		this.starUsage = starUsage;
	}

	public void delete(Long entryId, Long usageId) {
		deleteUsage.execute(entryId, usageId);
	}

	public void star(Long entryId, Long usageId) {
		starUsage.execute(entryId, usageId);
	}

}
