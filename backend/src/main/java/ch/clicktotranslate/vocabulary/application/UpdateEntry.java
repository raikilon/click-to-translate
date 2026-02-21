package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Entry;
import org.jmolecules.ddd.annotation.Service;

@Service
public class UpdateEntry {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public UpdateEntry(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public void execute(UpdateTerm update) {
		UpdateTerm validatedUpdate = requireUpdate(update);
		Entry entry = vocabularyRepository
			.findEntryById(userProvider.currentUserId(), Entry.Id.of(validatedUpdate.entryId()))
			.orElseThrow(EntryNotFoundException::new);
		entry.updateTerm(validatedUpdate.lemma());
		vocabularyRepository.saveEntry(entry);
	}

	private UpdateTerm requireUpdate(UpdateTerm update) {
		if (update == null) {
			throw new IllegalArgumentException("update must not be null");
		}
		if (update.lemma() == null || update.lemma().isBlank()) {
			throw new IllegalArgumentException("term must not be blank");
		}
		return update;
	}

}
