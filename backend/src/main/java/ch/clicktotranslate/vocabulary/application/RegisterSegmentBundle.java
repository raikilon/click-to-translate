package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.LemmaRepository;
import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.SegmentBundle;
import ch.clicktotranslate.vocabulary.domain.entity.Usage;

import java.util.List;
import java.util.Optional;

public class RegisterSegmentBundle {

    private final LemmaRepository lemmaRepository;

    public RegisterSegmentBundle(LemmaRepository lemmaRepository) {
        this.lemmaRepository = lemmaRepository;
    }

    public void execute(SegmentBundle event) {
        Optional<Lemma> existing = lemmaRepository.findByUserIdAndLemma(event.userId(), event.tokenizedWord());
        Lemma lemma = existing.orElseGet(
                () -> new Lemma(
                        event.userId(),
                        event.tokenizedWord(),
                        event.wordTranslation(),
                        event.sourceLanguage(),
                        event.targetLanguage(),
                        List.of()
                )
        );
        Usage usage = new Usage(lemma.id(), event.word(), event.wordTranslation(), event.sentence(),
                event.sentenceTranslation());
        lemma.addUsage(usage);
        lemmaRepository.save(lemma);
    }

}
