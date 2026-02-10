package ch.clicktotranslate.tokenizer.domain;

import org.jmolecules.ddd.annotation.Service;

@Service
public class SimpleWordTokenizer implements Tokenizer {

	@Override
	public String tokenize(String word) {
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
