package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.auth.UserProvider;
import ch.clicktotranslate.auth.UserId;
import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.Entry;
import org.jmolecules.ddd.annotation.Service;

@Service
public class DeleteUsage {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public DeleteUsage(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public void execute(Long entryId, Long usageId) {
		UserId userId = userProvider.currentUserId();
		Usage.Id id = Usage.Id.of(usageId);
		Entry entry = vocabularyRepository.findEntryById(userId, Entry.Id.of(entryId))
			.orElseThrow(EntryNotFoundException::new);
		boolean usageBelongsToEntry = entry.usages()
			.stream()
			.anyMatch(existing -> existing.id() != null && existing.id().equals(id));
		if (!usageBelongsToEntry) {
			throw new IllegalArgumentException("Usage not found for entry");
		}
		entry.removeUsage(id);
		vocabularyRepository.saveEntry(entry);
	}

}
