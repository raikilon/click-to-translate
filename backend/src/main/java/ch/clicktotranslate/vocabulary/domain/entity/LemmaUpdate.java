package ch.clicktotranslate.vocabulary.domain.entity;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record LemmaUpdate(String userId, Long lemmaId, String lemma, String lemmaTranslation) {
}
