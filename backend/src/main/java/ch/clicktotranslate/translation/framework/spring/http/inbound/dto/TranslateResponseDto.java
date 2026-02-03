package ch.clicktotranslate.translation.framework.spring.http.inbound.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TranslateResponseDto {
    private String word;

    private String sentence;

    private String wordTranslation;

    private String sentenceTranslation;

}
