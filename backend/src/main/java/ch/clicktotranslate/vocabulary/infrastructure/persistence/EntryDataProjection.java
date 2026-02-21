package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public interface EntryDataProjection {

	Long getId();

	String getUserId();

	String getLanguage();

	String getTerm();

	Optional<String> getTermCustomization();

	Set<JpaTermTranslation> getTranslations();

	Instant getLastEdit();

	Instant getCreatedAt();

}
