package ch.clicktotranslate.lemmatizer.application;

import org.jmolecules.ddd.annotation.Service;

@Service
public class SimpleWordLemmatizer implements Lemmatizer {

	@Override
	public String lemmatize(String word) {
		if (word == null) {
			return null;
		}
		String trimmed = word.trim();
		if (trimmed.isEmpty()) {
			return trimmed;
		}
		String[] tokens = trimmed.split("\\s+");
		return tokens[0];
	}

}
