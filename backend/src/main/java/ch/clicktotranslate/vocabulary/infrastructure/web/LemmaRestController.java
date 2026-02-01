package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.web.LemmaController;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaDeletion;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUpdate;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyClear;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyItem;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyQuery;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lemma")
public class LemmaRestController {

	private final LemmaController lemmaController;

	private final LemmaDtoMapper lemmaDtoMapper;

	public LemmaRestController(LemmaController lemmaController, LemmaDtoMapper lemmaDtoMapper) {
		this.lemmaController = lemmaController;
		this.lemmaDtoMapper = lemmaDtoMapper;
	}

	@PostMapping("/list")
	public List<LemmaItemDto> list(@RequestBody LemmaQueryDto request) {
		VocabularyQuery query = lemmaDtoMapper.toQuery(request);
		List<VocabularyItem> output = lemmaController.list(query);
		return lemmaDtoMapper.toDto(output);
	}

	@PostMapping("/update")
	public void update(@RequestBody LemmaUpdateDto request) {
		LemmaUpdate update = lemmaDtoMapper.toUpdate(request);
		lemmaController.update(update);
	}

	@PostMapping("/delete")
	public void delete(@RequestBody LemmaDeleteDto request) {
		LemmaDeletion delete = lemmaDtoMapper.toDelete(request);
		lemmaController.delete(delete);
	}

	@PostMapping("/clear")
	public void clear(@RequestBody LemmaClearDto request) {
		VocabularyClear clear = lemmaDtoMapper.toClear(request);
		lemmaController.clear(clear);
	}

}
