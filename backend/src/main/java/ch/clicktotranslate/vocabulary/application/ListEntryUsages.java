package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.auth.UserProvider;
import ch.clicktotranslate.auth.UserId;
import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.Entry;
import org.jmolecules.ddd.annotation.Service;

@Service
public class ListEntryUsages {

	private final VocabularyRepository vocabularyRepository;

	private final EntryQuery entryQuery;

	private final UserProvider userProvider;

	public ListEntryUsages(VocabularyRepository vocabularyRepository, EntryQuery entryQuery,
			UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.entryQuery = entryQuery;
		this.userProvider = userProvider;
	}

	public PageResult<Usage> execute(Long entryId, PageRequest pageRequest) {
		UserId userId = userProvider.currentUserId();
		Entry.Id id = Entry.Id.of(entryId);
		if (!vocabularyRepository.existsEntryById(userId, id)) {
			throw new EntryNotFoundException();
		}
		return entryQuery.findUsagesByEntry(userId, id, pageRequest);
	}

}
