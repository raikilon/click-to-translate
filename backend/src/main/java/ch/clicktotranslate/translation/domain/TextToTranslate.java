package ch.clicktotranslate.translation.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record TextToTranslate(String text, String sourceLanguage, String targetLanguage) {
}
