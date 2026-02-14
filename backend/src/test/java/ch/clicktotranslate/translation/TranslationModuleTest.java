package ch.clicktotranslate.translation;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import ch.clicktotranslate.translation.application.TextTranslationController;
import ch.clicktotranslate.translation.domain.TextToTranslate;
import ch.clicktotranslate.translation.infrastructure.LanguageDto;
import ch.clicktotranslate.translation.infrastructure.TextToTranslateDto;
import ch.clicktotranslate.translation.infrastructure.TextTranslationFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ApplicationModuleTest
class TranslationModuleTest {

	@Autowired
	private TextTranslationFacade textTranslationFacade;

	@Autowired
	private TextTranslationController textTranslationController;

	@MockitoBean
	private DeepLClient deepLClient;

	@Test
	void givenTextToTranslateDto_whenTranslateViaFacade_thenReturnsTranslationAndCallsProviderOnce()
			throws DeepLException, InterruptedException {
		TestContext context = new TestContext();
		context.givenTranslation();

		String response = textTranslationFacade.translate(context.facadeRequest);

		assertThat(response).isEqualTo(context.expectedTranslation);
		context.verifySingleTranslationCall();
	}

	@Test
	void givenTextToTranslate_whenTranslateViaController_thenReturnsTranslationAndCallsProviderOnce()
			throws DeepLException, InterruptedException {
		TestContext context = new TestContext();
		context.givenTranslation();

		String response = textTranslationController.translate(context.controllerRequest);

		assertThat(response).isEqualTo(context.expectedTranslation);
		context.verifySingleTranslationCall();
	}

	private final class TestContext {

		private final String text = "Das Haus ist gross.";

		private final String expectedTranslation = "The house is big.";

		private final String deepLSourceLanguage = "de";

		private final String deepLTargetLanguage = "en";

		private final TextResult deepLResponse = org.mockito.Mockito.mock(TextResult.class);

		private final TextToTranslateDto facadeRequest = new TextToTranslateDto(text, LanguageDto.DE, LanguageDto.EN);

		private final TextToTranslate controllerRequest = new TextToTranslate(text, "DE", "EN");

		private void givenTranslation() throws DeepLException, InterruptedException {
			given(deepLResponse.getText()).willReturn(expectedTranslation);
			given(deepLClient.translateText(text, deepLSourceLanguage, deepLTargetLanguage)).willReturn(deepLResponse);
		}

		private void verifySingleTranslationCall() throws DeepLException, InterruptedException {
			verify(deepLClient, times(1)).translateText(text, deepLSourceLanguage, deepLTargetLanguage);
		}

	}

}
