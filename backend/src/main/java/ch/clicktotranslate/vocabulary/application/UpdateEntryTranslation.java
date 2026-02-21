package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.UserId;
import ch.clicktotranslate.vocabulary.domain.Entry;
import org.jmolecules.ddd.annotation.Service;

@Service
public class UpdateEntryTranslation {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public UpdateEntryTranslation(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public void execute(TranslationUpdate update) {
		TranslationUpdate validatedUpdate = requireUpdate(update);
		UserId userId = userProvider.currentUserId();
		Entry entry = vocabularyRepository.findEntryById(userId, Entry.Id.of(validatedUpdate.entryId()))
			.orElseThrow(EntryNotFoundException::new);
		entry.setTranslation(validatedUpdate.targetLanguage(), validatedUpdate.translation());
		vocabularyRepository.saveEntry(entry);
	}

	private TranslationUpdate requireUpdate(TranslationUpdate update) {
		if (update == null) {
			throw new IllegalArgumentException("update must not be null");
		}
		if (update.targetLanguage() == null) {
			throw new IllegalArgumentException("targetLanguage must not be null");
		}
		if (update.translation() == null || update.translation().isBlank()) {
			throw new IllegalArgumentException("translation must not be blank");
		}
		return update;
	}

}
