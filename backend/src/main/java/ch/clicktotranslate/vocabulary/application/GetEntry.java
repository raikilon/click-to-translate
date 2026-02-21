package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Entry;
import org.jmolecules.ddd.annotation.Service;

@Service
public class GetEntry {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public GetEntry(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public Entry execute(Long entryId) {
		return vocabularyRepository.findEntryById(userProvider.currentUserId(), Entry.Id.of(entryId))
			.orElseThrow(EntryNotFoundException::new);
	}

}
