package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Entry;

public class ListEntries {

	private final EntryQuery entryQuery;

	private final UserProvider userProvider;

	public ListEntries(EntryQuery entryQuery, UserProvider userProvider) {
		this.entryQuery = entryQuery;
		this.userProvider = userProvider;
	}

	public PageResult<Entry> execute(PageRequest pageRequest) {
		return entryQuery.findEntriesByUser(userProvider.currentUserId(), pageRequest);
	}

}

