package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.UserLexeme;
import ch.clicktotranslate.vocabulary.domain.UserLexemeTranslation;
import ch.clicktotranslate.vocabulary.domain.TranslationUpdate;

public class SetEntryTranslation {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public SetEntryTranslation(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public void execute(TranslationUpdate update) {
		String userId = userProvider.currentUserId();
		UserLexeme userLexeme = vocabularyRepository.findUserLexemeByIdForUser(userId, update.entryId())
			.orElseThrow(() -> new IllegalArgumentException("Vocabulary entry not found"));
		vocabularyRepository.saveTranslationPreference(
				new UserLexemeTranslation(userId, userLexeme.lexemeId(), update.targetLanguage(), update.translation()));
	}

}


