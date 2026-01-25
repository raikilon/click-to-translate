package ch.clicktotranslate.translation.framework.spring.http.inbound;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordInput;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordOutput;
import ch.clicktotranslate.translation.framework.spring.http.inbound.dto.HttpTranslateRequest;
import ch.clicktotranslate.translation.framework.spring.http.inbound.dto.HttpTranslateResponse;
import ch.clicktotranslate.translation.framework.spring.http.inbound.mapper.HttpTranslateRequestMapper;
import ch.clicktotranslate.translation.framework.spring.http.inbound.mapper.HttpTranslateResponseMapper;
import ch.clicktotranslate.translation.infrastructure.controller.TranslateWordController;

@RestController
@RequestMapping("/translation")
public class TranslationHttpController {
	private final TranslateWordController translateWordController;
	private final HttpTranslateRequestMapper requestMapper;
	private final HttpTranslateResponseMapper responseMapper;

	public TranslationHttpController(TranslateWordController translateWordController, HttpTranslateRequestMapper requestMapper,
			HttpTranslateResponseMapper responseMapper) {
		this.translateWordController = translateWordController;
		this.requestMapper = requestMapper;
		this.responseMapper = responseMapper;
	}

	@PostMapping
	public HttpTranslateResponse translate(@RequestBody HttpTranslateRequest request) {
		TranslateWordInput input = requestMapper.map(request);
		TranslateWordOutput responseModel = translateWordController.translate(input);
		return responseMapper.map(responseModel);
	}
}
