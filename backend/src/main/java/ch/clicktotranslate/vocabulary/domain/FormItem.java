package ch.clicktotranslate.vocabulary.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record FormItem(Long formId, String form) {
}

