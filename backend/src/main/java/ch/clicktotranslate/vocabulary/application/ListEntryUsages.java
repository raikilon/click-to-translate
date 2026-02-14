package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.UsageItem;
import java.util.List;

public class ListEntryUsages {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public ListEntryUsages(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public List<UsageItem> execute(Long entryId) {
		return vocabularyRepository.findUsagesByUserLexemeId(userProvider.currentUserId(), entryId)
			.stream()
			.map(usage -> new UsageItem(usage.id(), usage.userLexemeId(), usage.surfaceFormId(), usage.sentence(),
					usage.targetLanguage(), usage.translatedSentence(), usage.sourceStart(), usage.sourceEnd(),
					usage.translatedStart(), usage.translatedEnd()))
			.toList();
	}

}


