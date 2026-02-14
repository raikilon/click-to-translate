package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.TextSpan;
import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.UsageUpdate;

public class UpdateUsage {

	private final VocabularyRepository vocabularyRepository;

	private final UserProvider userProvider;

	public UpdateUsage(VocabularyRepository vocabularyRepository, UserProvider userProvider) {
		this.vocabularyRepository = vocabularyRepository;
		this.userProvider = userProvider;
	}

	public void execute(UsageUpdate update) {
		String userId = userProvider.currentUserId();
		Usage existing = vocabularyRepository.findUsageByIdForUser(userId, update.usageId())
			.orElseThrow(() -> new IllegalArgumentException("Usage not found"));
		TextSpan translatedSpan = TextSpan.fromBounds(update.translatedStart(), update.translatedEnd(),
				update.translatedSentence(), "translated");
		String translatedToken = update.translatedSentence().substring(translatedSpan.start(), translatedSpan.end()).trim();
		Usage toSave = new Usage(existing.id(), existing.userLexemeId(), existing.surfaceFormId(), update.sentence(),
				update.targetLanguage(), update.translatedSentence(),
				TextSpan.fromBounds(update.sourceStart(), update.sourceEnd(), update.sentence(), "source"), translatedSpan,
				translatedToken);
		vocabularyRepository.saveUsage(toSave);
	}

}


