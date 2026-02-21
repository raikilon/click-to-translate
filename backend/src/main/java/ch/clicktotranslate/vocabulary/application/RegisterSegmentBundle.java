package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.Term;
import ch.clicktotranslate.vocabulary.domain.SegmentBundle;
import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.Entry;
import org.jmolecules.ddd.annotation.Service;

import java.util.Optional;

@Service
public class RegisterSegmentBundle {

	private final VocabularyRepository vocabularyRepository;

	public RegisterSegmentBundle(VocabularyRepository vocabularyRepository) {
		this.vocabularyRepository = vocabularyRepository;
	}

	public void execute(SegmentBundle event) {
		Term term = new Term(event.sourceLanguage(), event.tokenizedWord());
		Usage usage = new Usage(event.sentence(), event.word(), event.sentenceTranslation(), event.wordTranslation(),
				event.targetLanguage());

		Optional<Entry> existingEntry = vocabularyRepository.findEntryByTerm(event.userId(),
				term);

		if (existingEntry.isEmpty()) {
			Entry newEntry = Entry.createNew(event.userId(), term);
			newEntry.addUsage(usage);
			vocabularyRepository.saveEntry(newEntry);
			return;
		}

		Entry entry = existingEntry.get();
		boolean usageAlreadyExists = entry.usages().stream()
			.anyMatch(existingUsage -> isExactUsageMatch(existingUsage, usage));
		if (usageAlreadyExists) {
			return;
		}
		entry.addUsage(usage);
		vocabularyRepository.saveEntry(entry);
	}

	private boolean isExactUsageMatch(Usage existingUsage, Usage newUsage) {
		return existingUsage.sentence().equals(newUsage.sentence())
				&& existingUsage.sentenceSpan().equals(newUsage.sentenceSpan())
				&& existingUsage.translation().equals(newUsage.translation())
				&& existingUsage.translationSpan().equals(newUsage.translationSpan())
				&& existingUsage.targetLanguage() == newUsage.targetLanguage();
	}

}


