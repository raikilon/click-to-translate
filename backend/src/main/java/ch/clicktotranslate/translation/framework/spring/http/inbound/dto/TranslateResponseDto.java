package ch.clicktotranslate.translation.framework.spring.http.inbound.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TranslateResponseDto {
    private String wordSurface;

    private String wordTranslation;

    private String sentenceTranslation;

}
