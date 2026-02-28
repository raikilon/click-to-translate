package ch.clicktotranslate.translation.infrastructure;

import org.springframework.modulith.NamedInterface;

@NamedInterface
public record TextToTranslateDto(String text, LanguageDto sourceLanguage, LanguageDto targetLanguage, String context) {

}
