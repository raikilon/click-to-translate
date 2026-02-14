package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.Entry;

public class DeleteEntry {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public DeleteEntry(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public void execute(Long entryId) {
		vocabularyRepository.deleteEntryById(userProvider.currentUserId(), Entry.Id.of(entryId));
	}

}


