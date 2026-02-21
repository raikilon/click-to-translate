package ch.clicktotranslate.vocabulary.application;

import java.util.List;

public record PageResult<T>(List<T> items, int page, int size, long totalItems, int totalPages,
		boolean hasNext) {

	public PageResult {
		items = items == null ? List.of() : List.copyOf(items);
	}

}
