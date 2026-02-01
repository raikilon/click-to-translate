package ch.clicktotranslate.translation.application;

import java.util.List;

import java.util.EnumMap;
import java.util.Map;

import ch.clicktotranslate.translation.domain.TextToTranslate;
import ch.clicktotranslate.translation.domain.TextTranslation;
import ch.clicktotranslate.translation.application.translation.provider.TextTranslationProviderType;
import ch.clicktotranslate.translation.application.translation.TextTranslationProvider;
import org.jmolecules.ddd.annotation.Service;

@Service
public class DefaultTextTranslation implements TextTranslation {

	private final Map<TextTranslationProviderType, TextTranslationProvider> textTranslationByProvider;

	private final TextTranslationProviderType defaultProvider;

	public DefaultTextTranslation(List<TextTranslationProvider> strategyList,
			TextTranslationProviderType defaultProvider) {
		this.defaultProvider = defaultProvider;
		this.textTranslationByProvider = new EnumMap<>(TextTranslationProviderType.class);
		for (TextTranslationProvider strategy : strategyList) {
			this.textTranslationByProvider.put(strategy.getProvider(), strategy);
		}
	}

	@Override
	public String translate(TextToTranslate request) {
		TextTranslationProvider textTranslation = textTranslationByProvider.get(defaultProvider);
		if (textTranslation == null) {
			throw new IllegalStateException(
					"No translation textTranslation configured for provider: " + defaultProvider);
		}
		return textTranslation.translate(request);
	}

}
