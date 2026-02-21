package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Entry;
import org.jmolecules.ddd.annotation.Service;

@Service
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


