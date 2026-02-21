package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Usage;

public class VocabularyUsageController {

	private final ListEntryUsages listEntryUsages;

	private final DeleteUsage deleteUsage;

	public VocabularyUsageController(ListEntryUsages listEntryUsages,
			DeleteUsage deleteUsage) {
		this.listEntryUsages = listEntryUsages;
		this.deleteUsage = deleteUsage;
	}

	public PageResult<Usage> listByEntry(Long entryId, PageRequest pageRequest) {
		return listEntryUsages.execute(entryId, pageRequest);
	}

	public void delete(Long entryId, Long usageId) {
		deleteUsage.execute(entryId, usageId);
	}

}


