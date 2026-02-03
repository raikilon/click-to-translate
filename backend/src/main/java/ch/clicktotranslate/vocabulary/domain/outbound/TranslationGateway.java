package ch.clicktotranslate.vocabulary.domain.outbound;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.domain.entity.TranslatedLemma;

public interface TranslationGateway {
  TranslatedLemma translate(Lemma lemma);
}
