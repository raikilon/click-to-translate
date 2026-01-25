package ch.clicktotranslate.translation.infrastructure.gateway;

import ch.clicktotranslate.translation.infrastructure.gateway.client.DeepLApiClient;
import ch.clicktotranslate.translation.domain.outbound.TranslationGateway;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordInput;
import ch.clicktotranslate.translation.domain.usecase.model.TranslateWordOutput;

public class DeepLTranslationGateway implements TranslationGateway {
	private final DeepLApiClient apiClient;

	public DeepLTranslationGateway(DeepLApiClient apiClient) {
		this.apiClient = apiClient;
	}

	@Override
	public TranslateWordOutput translate(TranslateWordInput input) {
		return null;
	}
}
