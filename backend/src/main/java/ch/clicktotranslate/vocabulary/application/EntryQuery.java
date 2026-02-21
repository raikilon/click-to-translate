package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.Entry;
import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.UserId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;

@Repository
public interface EntryQuery {

	PageResult<Entry> findEntriesByUser(UserId userId, PageRequest pageRequest);

	PageResult<Usage> findUsagesByEntry(UserId userId, Entry.Id entryId, PageRequest pageRequest);

	List<Entry> findByLanguage(UserId userId, Language sourceLanguage);

	List<Entry> search(UserId userId, String query);

}
