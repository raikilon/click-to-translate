package ch.clicktotranslate.translation.infrastructure.controller.mapper;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.infrastructure.controller.model.TranslateRequest;

public class TranslateWordMapper {

    public TranslateWord map(TranslateRequest request) {
        TranslateWord translateWord = new TranslateWord();
        translateWord.setUserId(request.getUserId());
        translateWord.setWord(request.getWord());
        translateWord.setSentence(request.getSentence());
        translateWord.setSourceLanguage(request.getSourceLanguage());
        translateWord.setTargetLanguage(request.getTargetLanguage());
        return translateWord;
    }
}

