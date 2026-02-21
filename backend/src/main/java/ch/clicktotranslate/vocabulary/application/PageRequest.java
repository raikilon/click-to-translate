package ch.clicktotranslate.vocabulary.application;

import java.util.List;

public record PageRequest(int page, int size, List<Sort> sort) {

	public PageRequest {
		if (page < 0) {
			throw new IllegalArgumentException("page must be >= 0");
		}
		if (size <= 0) {
			throw new IllegalArgumentException("size must be > 0");
		}
		sort = sort == null ? List.of() : List.copyOf(sort);
	}

	public record Sort(String field, Direction direction) {

		public Sort {
			if (field == null || field.isBlank()) {
				throw new IllegalArgumentException("field must not be blank");
			}
			if (direction == null) {
				throw new IllegalArgumentException("direction must not be null");
			}
		}

		public enum Direction {

			ASC, DESC

		}

	}

}
