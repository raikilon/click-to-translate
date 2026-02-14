package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;

public class DeleteUsage {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public DeleteUsage(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public void execute(Long usageId) {
		vocabularyRepository.deleteUsageByIdForUser(userProvider.currentUserId(), usageId);
	}

}

