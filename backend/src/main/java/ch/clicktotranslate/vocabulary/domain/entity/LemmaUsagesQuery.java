package ch.clicktotranslate.vocabulary.domain.entity;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record LemmaUsagesQuery(String userId, Long lemmaId) {
}
