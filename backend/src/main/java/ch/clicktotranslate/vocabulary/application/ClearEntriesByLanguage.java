package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;

public class ClearEntriesByLanguage {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public ClearEntriesByLanguage(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public void execute(Language sourceLanguage) {
		vocabularyRepository.deleteUserLexemesByUserAndLanguage(userProvider.currentUserId(), sourceLanguage);
	}

}

