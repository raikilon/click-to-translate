package ch.clicktotranslate.vocabulary.domain;

import java.util.Objects;
import java.util.Optional;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record TextSpan(int start, int end) {

	public TextSpan {
		if (start < 0) {
			throw new IllegalArgumentException("start must be >= 0");
		}
		if (end <= start) {
			throw new IllegalArgumentException("end must be greater than start");
		}
	}

	public int length() {
		return end - start;
	}

	public static TextSpan fromBounds(int start, int end, String text, String fieldName) {
		Objects.requireNonNull(fieldName, "fieldName");
		if (text == null) {
			throw new IllegalArgumentException(fieldName + " text must not be null");
		}
		if (end > text.length()) {
			throw new IllegalArgumentException(fieldName + " end out of bounds");
		}
		return new TextSpan(start, end);
	}

	public static Optional<TextSpan> find(String text, String fragment) {
		if (isBlank(text) || isBlank(fragment)) {
			return Optional.empty();
		}
		String trimmed = fragment.trim();
		int start = text.indexOf(trimmed);
		if (start < 0) {
			return Optional.empty();
		}
		return Optional.of(new TextSpan(start, start + trimmed.length()));
	}

	public static TextSpan findRequired(String text, String fragment, String fieldName) {
		return find(text, fragment)
			.orElseThrow(() -> new IllegalArgumentException(fieldName + " fragment not found in text"));
	}

	private static boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

}
