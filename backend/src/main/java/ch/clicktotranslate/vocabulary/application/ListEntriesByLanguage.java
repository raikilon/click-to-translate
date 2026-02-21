package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.UserId;
import org.jmolecules.ddd.annotation.Service;

import java.util.List;

@Service
public class ListEntriesByLanguage {

	private final EntryQuery entryQuery;

	private final UserProvider userProvider;

	public ListEntriesByLanguage(EntryQuery entryQuery, UserProvider userProvider) {
		this.entryQuery = entryQuery;
		this.userProvider = userProvider;
	}

	public List<EntryData> execute(Language language) {
		return entryQuery.findByLanguage(userProvider.currentUserId(), language);
	}

}

