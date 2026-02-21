package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.Entry;
import ch.clicktotranslate.vocabulary.domain.UserId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;

@Repository
public interface EntryQuery {

	List<Entry> findAll(UserId userId);

	List<Entry> findByLanguage(UserId userId, Language sourceLanguage);

	List<Entry> search(UserId userId, String query);

}

