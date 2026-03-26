package ch.clicktotranslate.vocabulary.domain;

final class LevenshteinDistanceCalculator {

	int calculate(String left, String right) {
		if (left == null || right == null) {
			throw new IllegalArgumentException("Text is null");
		}

		int[] previous = new int[right.length() + 1];
		int[] current = new int[right.length() + 1];

		for (int index = 0; index <= right.length(); index++) {
			previous[index] = index;
		}

		for (int leftIndex = 1; leftIndex <= left.length(); leftIndex++) {
			current[0] = leftIndex;
			char leftChar = left.charAt(leftIndex - 1);

			for (int rightIndex = 1; rightIndex <= right.length(); rightIndex++) {
				int substitutionCost = leftChar == right.charAt(rightIndex - 1) ? 0 : 1;
				int insertionCost = current[rightIndex - 1] + 1;
				int deletionCost = previous[rightIndex] + 1;
				int substitutionTotalCost = previous[rightIndex - 1] + substitutionCost;
				current[rightIndex] = Math.min(Math.min(insertionCost, deletionCost), substitutionTotalCost);
			}

			int[] swap = previous;
			previous = current;
			current = swap;
		}

		return previous[right.length()];
	}

}
