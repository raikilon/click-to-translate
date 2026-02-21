package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Entry;
import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.UserId;
import org.jmolecules.ddd.annotation.Service;

@Service
public class StarUsage {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public StarUsage(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public void execute(Long entryId, Long usageId) {
		UserId userId = userProvider.currentUserId();
		Entry entry = vocabularyRepository.findEntryById(userId, Entry.Id.of(entryId))
			.orElseThrow(EntryNotFoundException::new);
		entry.starUsage(Usage.Id.of(usageId));
		vocabularyRepository.saveEntry(entry);
	}

}
