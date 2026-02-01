package ch.clicktotranslate.vocabulary.domain;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.NewSegmentEvent;
import ch.clicktotranslate.vocabulary.domain.entity.Usage;
import java.util.List;
import java.util.Optional;

public class RegisterUsageFromTranslation {

	private final Lemmatizer lemmatizer;

	private final LemmaRepository lemmaRepository;

	public RegisterUsageFromTranslation(Lemmatizer lemmatizer, LemmaRepository lemmaRepository) {
		this.lemmatizer = lemmatizer;
		this.lemmaRepository = lemmaRepository;
	}

	public void execute(NewSegmentEvent event) {
		String lemmaValue = lemmatizer.lemmatize(event.word());
		Optional<Lemma> existing = lemmaRepository.findByUserIdAndLemma(event.userId(), lemmaValue);
		Lemma lemma = existing.orElseGet(() -> new Lemma(event.userId(), lemmaValue, event.wordTranslation(),
				event.sourceLanguage(), event.targetLanguage(), List.of()));
		Usage usage = new Usage(lemma.id(), event.word(), event.wordTranslation(), event.sentence(),
				event.sentenceTranslation());
		lemma.addUsage(usage);
		lemmaRepository.save(lemma);
	}

}
