package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.auth.UserProvider;
import ch.clicktotranslate.vocabulary.domain.Entry;
import org.jmolecules.ddd.annotation.Service;

@Service
public class SearchEntries {

	private final EntryQuery entryQuery;

	private final UserProvider userProvider;

	public SearchEntries(EntryQuery entryQuery, UserProvider userProvider) {
		this.entryQuery = entryQuery;
		this.userProvider = userProvider;
	}

	public PageResult<Entry> execute(String query, PageRequest pageRequest) {
		return entryQuery.search(userProvider.currentUserId(), requireQuery(query), pageRequest);
	}

	private String requireQuery(String query) {
		if (query == null) {
			throw new IllegalArgumentException("query must not be null");
		}
		String trimmedQuery = query.trim();
		if (trimmedQuery.length() < 3) {
			throw new IllegalArgumentException("query must be at least 3 characters");
		}
		return trimmedQuery;
	}

}
