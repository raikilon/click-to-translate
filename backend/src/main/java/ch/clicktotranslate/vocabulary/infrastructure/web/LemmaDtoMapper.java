package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.domain.entity.LemmaDeletion;
import ch.clicktotranslate.vocabulary.domain.entity.LemmaUpdate;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyClear;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyItem;
import ch.clicktotranslate.vocabulary.domain.entity.VocabularyQuery;
import java.util.List;

public class LemmaDtoMapper {

	public VocabularyQuery toQuery(LemmaQueryDto request) {
		return new VocabularyQuery(request.userId());
	}

	public LemmaUpdate toUpdate(LemmaUpdateDto request) {
		return new LemmaUpdate(request.userId(), request.lemmaId(), request.lemma(), request.lemmaTranslation());
	}

	public LemmaDeletion toDelete(LemmaDeleteDto request) {
		return new LemmaDeletion(request.userId(), request.lemmaId());
	}

	public VocabularyClear toClear(LemmaClearDto request) {
		return new VocabularyClear(request.userId(), request.sourceLanguage());
	}

	public List<LemmaItemDto> toDto(List<VocabularyItem> output) {
		return output.stream()
			.map(item -> new LemmaItemDto(item.lemmaId(), item.lemma(), item.lemmaTranslation(), item.sourceLanguage(),
					item.targetLanguage()))
			.toList();
	}

}
