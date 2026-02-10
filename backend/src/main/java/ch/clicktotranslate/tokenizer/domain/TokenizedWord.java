package ch.clicktotranslate.tokenizer.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record TokenizedWord(String tokenizedWord, String tokenizedWordTranslation) {
}
