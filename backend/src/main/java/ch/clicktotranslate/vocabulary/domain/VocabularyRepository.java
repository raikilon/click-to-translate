package ch.clicktotranslate.vocabulary.domain;

import java.util.Optional;

public interface VocabularyRepository {

	Optional<Entry> findEntryByTerm(UserId userId, Term term);

	void saveEntry(Entry entry);

	Optional<Entry> findEntryById(UserId userId, Entry.Id entryId);

	void deleteEntryById(UserId userId, Entry.Id entryId);

}


