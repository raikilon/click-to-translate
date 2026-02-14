package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.UsageItem;
import ch.clicktotranslate.vocabulary.domain.UsageUpdate;
import java.util.List;

public class VocabularyUsageController {

	private final ListEntryUsages listEntryUsages;

	private final UpdateUsage updateUsage;

	private final DeleteUsage deleteUsage;

	public VocabularyUsageController(ListEntryUsages listEntryUsages, UpdateUsage updateUsage,
			DeleteUsage deleteUsage) {
		this.listEntryUsages = listEntryUsages;
		this.updateUsage = updateUsage;
		this.deleteUsage = deleteUsage;
	}

	public List<UsageItem> listByEntry(Long entryId) {
		return listEntryUsages.execute(entryId);
	}

	public void update(UsageUpdate update) {
		updateUsage.execute(update);
	}

	public void delete(Long usageId) {
		deleteUsage.execute(usageId);
	}

}

