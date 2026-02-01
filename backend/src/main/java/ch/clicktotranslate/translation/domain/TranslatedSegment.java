package ch.clicktotranslate.translation.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record TranslatedSegment(String word, String sentence, String translatedWord, String translatedSentence) {
}
