package ch.clicktotranslate.vocabulary.domain;

import java.util.ArrayList;
import java.util.List;

final class TextTokenizer {

	List<TextToken> tokenize(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Text is null");
		}

		List<TextToken> tokens = new ArrayList<>();
		int start = -1;

		for (int index = 0; index < text.length(); index++) {
			char current = text.charAt(index);
			if (isTokenCharacter(current)) {
				if (start < 0) {
					start = index;
				}
				continue;
			}

			if (start >= 0) {
				tokens.add(new TextToken(text.substring(start, index), start, index));
				start = -1;
			}
		}

		if (start >= 0) {
			tokens.add(new TextToken(text.substring(start), start, text.length()));
		}

		return tokens;
	}

	private boolean isTokenCharacter(char current) {
		return Character.isLetterOrDigit(current) || current == '\'';
	}

}
