package ch.clicktotranslate.translation.framework.spring.http.inbound;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.framework.spring.http.inbound.dto.TranslateRequestDto;
import ch.clicktotranslate.translation.framework.spring.http.inbound.dto.TranslateResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.clicktotranslate.translation.domain.entity.TranslatedWord;
import ch.clicktotranslate.translation.framework.spring.http.inbound.mapper.HttpTranslateRequestMapper;
import ch.clicktotranslate.translation.framework.spring.http.inbound.mapper.HttpTranslateResponseMapper;
import ch.clicktotranslate.translation.infrastructure.controller.TranslationController;
import ch.clicktotranslate.translation.infrastructure.controller.model.TranslateRequest;

@RestController
@RequestMapping("/api/translate")
public class TranslationHttpController {
	private final TranslationController translationController;
	private final HttpTranslateRequestMapper requestMapper;
	private final HttpTranslateResponseMapper responseMapper;

	public TranslationHttpController(TranslationController translationController, HttpTranslateRequestMapper requestMapper,
									 HttpTranslateResponseMapper responseMapper) {
		this.translationController = translationController;
		this.requestMapper = requestMapper;
		this.responseMapper = responseMapper;
	}

	@PostMapping
	public TranslateResponseDto translate(@RequestBody TranslateRequestDto translateRequestDto) {
		TranslateRequest translateRequest = requestMapper.map(translateRequestDto);
		TranslatedWord translatedWord = translationController.translate(translateRequest);
		return responseMapper.map(translatedWord);
	}
}
