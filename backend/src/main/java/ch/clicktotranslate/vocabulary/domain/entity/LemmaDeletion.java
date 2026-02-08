package ch.clicktotranslate.vocabulary.domain.entity;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record LemmaDeletion(String userId, Long lemmaId) {
}
