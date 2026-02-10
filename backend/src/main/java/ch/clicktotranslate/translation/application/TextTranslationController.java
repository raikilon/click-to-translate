package ch.clicktotranslate.translation.application;

import java.util.List;

import java.util.EnumMap;
import java.util.Map;

import ch.clicktotranslate.translation.domain.TextToTranslate;
import org.jmolecules.ddd.annotation.Service;

@Service
public class TextTranslationController {

	private final Map<TextTranslationProviderType, TextTranslationProvider> textTranslationByProvider;

	private final TextTranslationProviderType defaultProvider;

	public TextTranslationController(List<TextTranslationProvider> strategyList,
			TextTranslationProviderType defaultProvider) {
		this.defaultProvider = defaultProvider;
		this.textTranslationByProvider = new EnumMap<>(TextTranslationProviderType.class);
		for (TextTranslationProvider strategy : strategyList) {
			this.textTranslationByProvider.put(strategy.getProvider(), strategy);
		}
	}

	public String translate(TextToTranslate request) {
		TextTranslationProvider textTranslation = textTranslationByProvider.get(defaultProvider);
		if (textTranslation == null) {
			throw new IllegalStateException(
					"No translation textTranslation configured for provider: " + defaultProvider);
		}
		return textTranslation.translate(request);
	}

}
