package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.UserId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;

@Repository
public interface EntryQuery {

	List<EntryData> findAll(UserId userId);

	List<EntryData> findByLanguage(UserId userId, Language sourceLanguage);

	List<EntryData> search(UserId userId, String query);

}

