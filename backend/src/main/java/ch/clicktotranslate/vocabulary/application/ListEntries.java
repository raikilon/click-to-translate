package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.UserId;
import java.util.List;

public class ListEntries {

	private final EntryQuery entryQuery;

	private final UserProvider userProvider;

	public ListEntries(EntryQuery entryQuery, UserProvider userProvider) {
		this.entryQuery = entryQuery;
		this.userProvider = userProvider;
	}

	public List<EntryData> execute() {
		return entryQuery.findAll(userProvider.currentUserId());
	}

}

