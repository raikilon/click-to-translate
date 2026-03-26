package ch.clicktotranslate.vocabulary.domain;

import java.util.Locale;
import java.util.Optional;

final class FuzzyTextSpanFinder {

	private final TextTokenizer textTokenizer;

	private final LevenshteinDistanceCalculator levenshteinDistanceCalculator;

	private final int minFuzzyFragmentLength;

	private final float maxDistanceRatio;

	FuzzyTextSpanFinder(TextTokenizer textTokenizer, LevenshteinDistanceCalculator levenshteinDistanceCalculator,
			int minFuzzyFragmentLength, float maxDistanceRatio) {
		if (textTokenizer == null || levenshteinDistanceCalculator == null) {
			throw new IllegalArgumentException("Dependency is null");
		}
		if (minFuzzyFragmentLength < 1) {
			throw new IllegalArgumentException("minFuzzyFragmentLength must be >= 1");
		}
		if (maxDistanceRatio <= 0) {
			throw new IllegalArgumentException("maxDistanceRatio must be > 0");
		}
		this.textTokenizer = textTokenizer;
		this.levenshteinDistanceCalculator = levenshteinDistanceCalculator;
		this.minFuzzyFragmentLength = minFuzzyFragmentLength;
		this.maxDistanceRatio = maxDistanceRatio;
	}

	static FuzzyTextSpanFinder conservative() {
		return new FuzzyTextSpanFinder(new TextTokenizer(), new LevenshteinDistanceCalculator(), 4, 0.4f);
	}

	Optional<TextSpan> findClosestIgnoreCase(String text, String fragment) {
		if (isBlank(text) || isBlank(fragment)) {
			throw new IllegalArgumentException("Text is null");
		}

		if (fragment.length() < minFuzzyFragmentLength) {
			return Optional.empty();
		}

		String normalizedFragment = fragment.toLowerCase(Locale.ROOT);
		int maxDistance = maxAllowedDistance(normalizedFragment.length());
		Candidate bestCandidate = null;
		boolean hasAmbiguousBest = false;

		for (TextToken token : textTokenizer.tokenize(text)) {
			String normalizedCandidate = token.value().toLowerCase(Locale.ROOT);
			int distance = levenshteinDistanceCalculator.calculate(normalizedCandidate, normalizedFragment);
			if (distance > maxDistance) {
				continue;
			}

			Candidate scoredCandidate = new Candidate(token.start(), token.end(), distance,
					Math.abs(normalizedCandidate.length() - normalizedFragment.length()));

			if (bestCandidate == null || scoredCandidate.isBetterThan(bestCandidate)) {
				bestCandidate = scoredCandidate;
				hasAmbiguousBest = false;
				continue;
			}

			if (scoredCandidate.isEquivalentTo(bestCandidate)) {
				hasAmbiguousBest = true;
			}
		}

		if (bestCandidate == null || hasAmbiguousBest) {
			return Optional.empty();
		}

		return Optional.of(new TextSpan(bestCandidate.start(), bestCandidate.end()));
	}

	private int maxAllowedDistance(int fragmentLength) {
		return Math.max(1, Math.round(fragmentLength * maxDistanceRatio));
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

	private record Candidate(int start, int end, int distance, int lengthDifference) {

		private boolean isBetterThan(Candidate other) {
			if (distance != other.distance()) {
				return distance < other.distance();
			}
			if (lengthDifference != other.lengthDifference()) {
				return lengthDifference < other.lengthDifference();
			}
			return start < other.start();
		}

		private boolean isEquivalentTo(Candidate other) {
			return distance == other.distance() && lengthDifference == other.lengthDifference();
		}

	}

}
