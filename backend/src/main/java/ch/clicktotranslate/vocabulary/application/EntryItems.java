package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.Lexeme;
import ch.clicktotranslate.vocabulary.domain.UserLexeme;
import ch.clicktotranslate.vocabulary.domain.UserLexemeTranslation;
import ch.clicktotranslate.vocabulary.domain.EntryItem;
import java.util.ArrayList;
import java.util.List;

public class EntryItems {

	private final VocabularyRepository vocabularyRepository;

	public EntryItems(VocabularyRepository vocabularyRepository) {
		this.vocabularyRepository = vocabularyRepository;
	}

	public List<EntryItem> from(String userId, List<UserLexeme> userLexemes) {
		List<EntryItem> items = new ArrayList<>();
		for (UserLexeme userLexeme : userLexemes) {
			Lexeme lexeme = vocabularyRepository.findLexemeById(userLexeme.lexemeId()).orElse(null);
			if (lexeme == null) {
				continue;
			}
			String effectiveTranslation = vocabularyRepository.findAnyTranslationPreference(userId, lexeme.id())
				.map(UserLexemeTranslation::gloss)
				.orElseGet(() -> vocabularyRepository.findDefaultTranslatedTokenByUserLexemeId(userId, userLexeme.id())
					.orElse(null));
			items.add(new EntryItem(userLexeme.id(), lexeme.id(), lexeme.lemma(), lexeme.language(),
					effectiveTranslation));
		}
		return items;
	}

}


