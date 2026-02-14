package ch.clicktotranslate.vocabulary.domain;

import org.jmolecules.ddd.types.ValueObject;

public record TextSpan(int start, int end) implements ValueObject {

	public TextSpan {
		if (start < 0) {
			throw new IllegalArgumentException("start must be >= 0");
		}
		if (end <= start) {
			throw new IllegalArgumentException("end must be greater than start");
		}
	}

	public static TextSpan find(String text, String fragment) {
		if (isBlank(text) || isBlank(fragment)) {
			throw new IllegalArgumentException("Text is null");
		}
		int start = text.indexOf(fragment);
		if (start < 0) {
			throw new IllegalArgumentException(String.format("%s not found in text", fragment));
		}
		return new TextSpan(start, start + fragment.length());
	}

	private static boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

}
