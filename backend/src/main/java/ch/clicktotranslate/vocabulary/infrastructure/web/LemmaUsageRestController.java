package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.web.LemmaUsageController;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageDeletion;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageItem;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsageUpdate;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUsagesQuery;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lemma-usage")
public class LemmaUsageRestController {

	private final LemmaUsageController lemmaUsageController;

	private final LemmaUsageDtoMapper lemmaUsageDtoMapper;

	public LemmaUsageRestController(LemmaUsageController lemmaUsageController,
			LemmaUsageDtoMapper lemmaUsageDtoMapper) {
		this.lemmaUsageController = lemmaUsageController;
		this.lemmaUsageDtoMapper = lemmaUsageDtoMapper;
	}

	@PostMapping("/list")
	public List<LemmaUsageItemDto> list(@RequestBody LemmaUsagesQueryDto request) {
		LemmaUsagesQuery query = lemmaUsageDtoMapper.toQuery(request);
		List<LemmaUsageItem> items = lemmaUsageController.list(query);
		return lemmaUsageDtoMapper.toDto(items);
	}

	@PostMapping("/update")
	public void update(@RequestBody LemmaUsageUpdateDto request) {
		LemmaUsageUpdate update = lemmaUsageDtoMapper.toUpdate(request);
		lemmaUsageController.update(update);
	}

	@PostMapping("/delete")
	public void delete(@RequestBody LemmaUsageDeleteDto request) {
		LemmaUsageDeletion delete = lemmaUsageDtoMapper.toDelete(request);
		lemmaUsageController.delete(delete);
	}

}
