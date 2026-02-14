package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Usage;

import java.util.List;

public class VocabularyUsageController {

	private final ListEntryUsages listEntryUsages;

	private final DeleteUsage deleteUsage;

	public VocabularyUsageController(ListEntryUsages listEntryUsages,
			DeleteUsage deleteUsage) {
		this.listEntryUsages = listEntryUsages;
		this.deleteUsage = deleteUsage;
	}

	public List<Usage> listByEntry(Long entryId) {
		return listEntryUsages.execute(entryId);
	}

	public void delete(Long entryId, Long usageId) {
		deleteUsage.execute(entryId, usageId);
	}

}


