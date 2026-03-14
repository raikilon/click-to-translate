package ch.clicktotranslate.translation.application;

import ch.clicktotranslate.translation.domain.Language;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeepLLanguageMapperTest {

	@Test
	void givenEnLanguage_whenToDeepLCode_thenReturnsEnGb() {
		TestContext context = new TestContext();

		String deepLCode = context.underTest.toDeepLCode(Language.EN);

		assertThat(deepLCode).isEqualTo("en-GB");
	}

	@Test
	void givenNonEnLanguage_whenToDeepLCode_thenReturnsDefaultMapping() {
		TestContext context = new TestContext();

		String deepLCode = context.underTest.toDeepLCode(Language.DE);

		assertThat(deepLCode).isEqualTo("de");
	}

	private static final class TestContext {

		private final DeepLLanguageMapper underTest = new DeepLLanguageMapper();

	}

}
