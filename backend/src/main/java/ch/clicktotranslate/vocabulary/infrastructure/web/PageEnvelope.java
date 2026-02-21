package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.PageResult;
import java.util.List;
import java.util.function.Function;

public record PageEnvelope<T>(List<T> items, int page, int size, long totalItems, int totalPages, boolean hasNext) {

	public static <A, R> PageEnvelope<R> from(PageResult<A> pageResult, Function<A, R> mapper) {
		return new PageEnvelope<>(pageResult.items().stream().map(mapper).toList(), pageResult.page(),
				pageResult.size(), pageResult.totalItems(), pageResult.totalPages(), pageResult.hasNext());
	}

}
