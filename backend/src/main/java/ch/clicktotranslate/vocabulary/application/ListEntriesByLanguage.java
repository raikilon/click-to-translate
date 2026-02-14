package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.EntryItem;
import java.util.List;
import java.util.Optional;

public class ListEntriesByLanguage {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	private final EntryItems entryItems;

	public ListEntriesByLanguage(VocabularyRepository vocabularyRepository, UserProvider userProvider,
			EntryItems entryItems) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
		this.entryItems = entryItems;
	}

	public List<EntryItem> execute(Language sourceLanguage) {
		String userId = userProvider.currentUserId();
		return entryItems.from(userId, vocabularyRepository.findUserLexemes(userId, Optional.of(sourceLanguage)));
	}

}


