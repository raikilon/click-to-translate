package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.PageRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class PageRequestDtoMapper {

	public PageRequest toPageRequest(Pageable pageable) {
		List<PageRequest.Sort> sort = pageable.getSort()
			.stream()
			.map(order -> new PageRequest.Sort(order.getProperty(),
					order.getDirection().isAscending() ? PageRequest.Sort.Direction.ASC
							: PageRequest.Sort.Direction.DESC))
			.toList();
		if (sort.isEmpty()) {
			sort = List.of(new PageRequest.Sort("id", PageRequest.Sort.Direction.ASC));
		}
		return new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
	}

}
