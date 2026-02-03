package ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.mapper;

import ch.clicktotranslate.translation.infrastructure.service.strategy.deepl.dto.DeepLTranslateResponse;

public class DeepLTranslateResponseMapper {

    public String map(DeepLTranslateResponse response) {
        return response.getTranslatedText();
    }
}
