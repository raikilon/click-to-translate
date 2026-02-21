package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Entry;
import ch.clicktotranslate.vocabulary.domain.Term;
import ch.clicktotranslate.vocabulary.domain.UserId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface VocabularyRepository {

    Optional<Entry> findEntryByTerm(UserId userId, Term term);

    void saveEntry(Entry entry);

    Optional<Entry> findEntryById(UserId userId, Entry.Id entryId);

    boolean existsEntryById(UserId userId, Entry.Id entryId);

    void deleteEntryById(UserId userId, Entry.Id entryId);

}
