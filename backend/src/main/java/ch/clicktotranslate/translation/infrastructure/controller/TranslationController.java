package ch.clicktotranslate.translation.infrastructure.controller;

import ch.clicktotranslate.translation.domain.entity.TranslateWord;
import ch.clicktotranslate.translation.domain.entity.TranslatedWord;
import ch.clicktotranslate.translation.domain.usecase.TranslateWordUseCase;
import ch.clicktotranslate.translation.infrastructure.controller.mapper.TranslateWordMapper;
import ch.clicktotranslate.translation.infrastructure.controller.model.TranslateRequest;
import ch.clicktotranslate.translation.infrastructure.event.EventPublisher;
import ch.clicktotranslate.translation.infrastructure.event.TranslatedWordEvent;
import ch.clicktotranslate.translation.infrastructure.event.TranslatedWordEventMapper;

public class TranslationController {
	private final TranslateWordUseCase translateWordUseCase;
	private final EventPublisher eventPublisher;
	private final TranslatedWordEventMapper translatedWordEventMapper;
	private final  TranslateWordMapper translateWordMapper;
	public TranslationController(TranslateWordUseCase translateWordUseCase, EventPublisher eventPublisher,
			TranslatedWordEventMapper translatedWordEventMapper, TranslateWordMapper translateWordMapper) {
		this.translateWordUseCase = translateWordUseCase;
		this.eventPublisher = eventPublisher;
		this.translatedWordEventMapper = translatedWordEventMapper;
		this.translateWordMapper = translateWordMapper;
	}

	public TranslatedWord translate(TranslateRequest input) {
		TranslateWord translateWord = this.translateWordMapper.map(input);

		TranslatedWord translatedWord = translateWordUseCase.execute(translateWord);

		TranslatedWordEvent event = translatedWordEventMapper.map(input, translatedWord);

		eventPublisher.publish(event);

		return translatedWord;
	}

}
