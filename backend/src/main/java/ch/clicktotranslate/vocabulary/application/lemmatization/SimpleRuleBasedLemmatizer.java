package ch.clicktotranslate.vocabulary.application.lemmatization;

import ch.clicktotranslate.vocabulary.domain.Lemmatizer;

import java.util.Locale;

public class SimpleRuleBasedLemmatizer implements Lemmatizer {

	@Override
	public String lemmatize(String word) {
		if (word == null) {
			return null;
		}
		String normalized = word.strip();
		if (normalized.isEmpty()) {
			return normalized;
		}
		return normalized;
	}

}
