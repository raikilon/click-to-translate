package ch.clicktotranslate.vocabulary.domain.outbound;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.WordLinkToken;

import java.util.List;
import java.util.Optional;

public interface WordLinkTokenRepository {

	Optional<WordLinkToken> findById(Long id);

	List<WordLinkToken> findAll();

	WordLinkToken save(WordLinkToken wordLinkToken);

	void deleteById(Long id);

	void deleteAll();

}
