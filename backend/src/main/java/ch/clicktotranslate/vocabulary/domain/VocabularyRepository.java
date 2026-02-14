package ch.clicktotranslate.vocabulary.domain;

import java.util.List;
import java.util.Optional;

public interface VocabularyRepository {

	Lexeme findOrCreateLexeme(Language language, String lemma);

	UserLexeme findOrCreateUserLexeme(String userId, Long lexemeId);

	SurfaceForm findOrCreateSurfaceForm(Long lexemeId, String form);

	Usage saveUsage(Usage usage);

	Optional<Usage> findUsageByIdForUser(String userId, Long usageId);

	List<Usage> findUsagesByUserLexemeId(String userId, Long userLexemeId);

	void deleteUsageByIdForUser(String userId, Long usageId);

	Optional<UserLexeme> findUserLexemeByIdForUser(String userId, Long userLexemeId);

	Optional<Lexeme> findLexemeById(Long lexemeId);

	List<UserLexeme> findUserLexemes(String userId, Optional<Language> sourceLanguage);

	List<UserLexeme> searchUserLexemes(String userId, String query);

	List<SurfaceForm> findSurfaceFormsByUserLexemeId(String userId, Long userLexemeId);

	Optional<String> findDefaultTranslatedTokenByUserLexemeId(String userId, Long userLexemeId);

	void deleteUserLexemeByIdForUser(String userId, Long userLexemeId);

	void deleteUserLexemesByUserAndLanguage(String userId, Language sourceLanguage);

	Optional<UserLexemeTranslation> findAnyTranslationPreference(String userId, Long sourceLexemeId);

	Optional<UserLexemeTranslation> findTranslationPreference(String userId, Long sourceLexemeId,
			Language targetLanguage);

	UserLexemeTranslation saveTranslationPreference(UserLexemeTranslation translation);

}
