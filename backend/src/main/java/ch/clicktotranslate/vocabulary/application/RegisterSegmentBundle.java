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

		Optional<Entry> existingEntry = vocabularyRepository.findEntryByTerm(event.userId(), term);

		if (existingEntry.isEmpty()) {
			Entry newEntry = Entry.createNew(event.userId(), term);
			newEntry.addUsage(usage);
			vocabularyRepository.saveEntry(newEntry);
			return;
		}

		Entry entry = existingEntry.get();
		boolean usageAlreadyExists = entry.usages()
			.stream()
			.anyMatch(existing -> existing.sentence().equals(usage.sentence())
					&& existing.targetLanguage().equals(usage.targetLanguage()));
		if (usageAlreadyExists) {
			return;
		}
		if (entry.addUsage(usage)) {
			vocabularyRepository.saveEntry(entry);
		}
	}

}
