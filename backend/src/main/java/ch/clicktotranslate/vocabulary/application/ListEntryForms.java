package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.FormItem;
import java.util.List;

public class ListEntryForms {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public ListEntryForms(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public List<FormItem> execute(Long entryId) {
		return vocabularyRepository.findSurfaceFormsByUserLexemeId(userProvider.currentUserId(), entryId)
			.stream()
			.map(form -> new FormItem(form.id(), form.form()))
			.toList();
	}

}


