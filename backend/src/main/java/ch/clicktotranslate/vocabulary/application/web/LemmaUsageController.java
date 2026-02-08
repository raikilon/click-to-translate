package ch.clicktotranslate.vocabulary.application.web;

import ch.clicktotranslate.vocabulary.domain.DeleteUsage;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageDeletion;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageItem;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageUpdate;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsagesQuery;
import ch.clicktotranslate.vocabulary.domain.ListLemmaUsages;
import ch.clicktotranslate.vocabulary.domain.UpdateUsage;
import java.util.List;

public class LemmaUsageController {

	private final UpdateUsage updateUsage;

	private final DeleteUsage deleteUsage;

	private final ListLemmaUsages listLemmaUsages;

	public LemmaUsageController(UpdateUsage updateUsage, DeleteUsage deleteUsage, ListLemmaUsages listLemmaUsages) {
		this.updateUsage = updateUsage;
		this.deleteUsage = deleteUsage;
		this.listLemmaUsages = listLemmaUsages;
	}

	public List<LemmaUsageItem> list(LemmaUsagesQuery query) {
		return listLemmaUsages.execute(query);
	}

	public void update(LemmaUsageUpdate update) {
		updateUsage.execute(update);
	}

	public void delete(LemmaUsageDeletion delete) {
		deleteUsage.execute(delete);
	}

}
