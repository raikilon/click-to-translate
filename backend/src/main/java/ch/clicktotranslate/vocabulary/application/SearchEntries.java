package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.EntryItem;
import java.util.List;

public class SearchEntries {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	private final EntryItems entryItems;

	public SearchEntries(VocabularyRepository vocabularyRepository, UserProvider userProvider, EntryItems entryItems) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
		this.entryItems = entryItems;
	}

	public List<EntryItem> execute(String query) {
		String userId = userProvider.currentUserId();
		return entryItems.from(userId, vocabularyRepository.searchUserLexemes(userId, query));
	}

}


