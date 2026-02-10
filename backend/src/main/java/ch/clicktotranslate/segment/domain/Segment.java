package ch.clicktotranslate.segment.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Segment(String word, String sentence, String sourceLanguage, String targetLanguage) {
}
