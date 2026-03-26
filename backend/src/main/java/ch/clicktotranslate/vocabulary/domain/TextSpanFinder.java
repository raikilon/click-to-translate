package ch.clicktotranslate.vocabulary.domain;

import java.util.Optional;

final class TextSpanFinder {

	private final FuzzyTextSpanFinder fuzzyTextSpanFinder;

	TextSpanFinder(FuzzyTextSpanFinder fuzzyTextSpanFinder) {
		if (fuzzyTextSpanFinder == null) {
			throw new IllegalArgumentException("fuzzyTextSpanFinder is null");
		}
		this.fuzzyTextSpanFinder = fuzzyTextSpanFinder;
	}

	Optional<TextSpan> findIgnoreCaseOrClosest(String text, String fragment) {
		try {
			return Optional.of(TextSpan.findIgnoreCase(text, fragment));
		}
		catch (IllegalArgumentException ignored) {
			return fuzzyTextSpanFinder.findClosestIgnoreCase(text, fragment);
		}
	}

}
