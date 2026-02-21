package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.UserId;
import ch.clicktotranslate.vocabulary.domain.Entry;
import org.jmolecules.ddd.annotation.Service;

import java.util.List;

@Service
public class ListEntryUsages {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public ListEntryUsages(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public List<Usage> execute(Long entryId) {
		UserId userId = userProvider.currentUserId();
		Entry entry = vocabularyRepository.findEntryById(userId, Entry.Id.of(entryId))
			.orElseThrow(EntryNotFoundException::new);
		return entry.usages();
	}

}


