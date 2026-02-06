package ch.clicktotranslate.translation.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Segment(String word, String sentence, Language sourceLanguage, Language targetLanguage) {
}
