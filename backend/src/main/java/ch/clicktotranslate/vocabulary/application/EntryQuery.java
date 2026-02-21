package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.Entry;
import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.UserId;
import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface EntryQuery {

	PageResult<Entry> findEntriesByUser(UserId userId, PageRequest pageRequest);

	PageResult<Usage> findUsagesByEntry(UserId userId, Entry.Id entryId, PageRequest pageRequest);

	PageResult<Entry> findByLanguage(UserId userId, Language sourceLanguage, PageRequest pageRequest);

	PageResult<Entry> search(UserId userId, String query, PageRequest pageRequest);

}
