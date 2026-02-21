package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.Entry;
import org.jmolecules.ddd.annotation.Service;

@Service
public class ListEntriesByLanguage {

	private final EntryQuery entryQuery;

	private final UserProvider userProvider;

	public ListEntriesByLanguage(EntryQuery entryQuery, UserProvider userProvider) {
		this.entryQuery = entryQuery;
		this.userProvider = userProvider;
	}

	public PageResult<Entry> execute(Language language, PageRequest pageRequest) {
		return entryQuery.findByLanguage(userProvider.currentUserId(), language, pageRequest);
	}

}
