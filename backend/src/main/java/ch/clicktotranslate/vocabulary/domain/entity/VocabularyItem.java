package ch.clicktotranslate.vocabulary.domain.entity;

import org.jmolecules.ddd.annotation.ValueObject;
import ch.clicktotranslate.vocabulary.domain.Language;

@ValueObject
public record VocabularyItem(Long lemmaId, String lemma, String lemmaTranslation, Language sourceLanguage,
		Language targetLanguage) {
}
