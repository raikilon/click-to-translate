package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Usage;

public class VocabularyUsageController {

	private final ListEntryUsages listEntryUsages;

	private final DeleteUsage deleteUsage;

	private final StarUsage starUsage;

	public VocabularyUsageController(ListEntryUsages listEntryUsages, DeleteUsage deleteUsage, StarUsage starUsage) {
		this.listEntryUsages = listEntryUsages;
		this.deleteUsage = deleteUsage;
		this.starUsage = starUsage;
	}

	public PageResult<Usage> listByEntry(Long entryId, PageRequest pageRequest) {
		return listEntryUsages.execute(entryId, pageRequest);
	}

	public void delete(Long entryId, Long usageId) {
		deleteUsage.execute(entryId, usageId);
	}

	public void star(Long entryId, Long usageId) {
		starUsage.execute(entryId, usageId);
	}

}
