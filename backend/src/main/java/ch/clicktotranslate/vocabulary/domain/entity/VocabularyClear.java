package ch.clicktotranslate.vocabulary.domain.entity;

import org.jmolecules.ddd.annotation.ValueObject;
import ch.clicktotranslate.vocabulary.domain.Language;

@ValueObject
public record VocabularyClear(String userId, Language sourceLanguage) {
}
