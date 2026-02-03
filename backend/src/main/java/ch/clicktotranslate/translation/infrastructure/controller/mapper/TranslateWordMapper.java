package ch.clicktotranslate.translation.infrastructure.controller.mapper;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.infrastructure.controller.model.TranslateRequest;

public class TranslateWordMapper {

    public TranslateWord map(TranslateRequest request) {
        return new TranslateWord(
                request.userId(),
                request.word(),
                request.sentence(),
                request.sourceLanguage(),
                request.targetLanguage()
        );
    }
}

