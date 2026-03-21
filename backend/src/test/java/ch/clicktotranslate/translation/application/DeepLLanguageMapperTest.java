package ch.clicktotranslate.translation.application;

import ch.clicktotranslate.translation.domain.Language;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeepLLanguageMapperTest {

	@Test
	void givenEnLanguage_whenToDeepLCode_thenReturnsUppercaseCode() {
		TestContext context = new TestContext();

		String deepLCode = context.underTest.toDeepLCode(Language.EN);

		assertThat(deepLCode).isEqualTo("EN");
	}

	@Test
	void givenNonEnLanguage_whenToDeepLCode_thenReturnsUppercaseCode() {
		TestContext context = new TestContext();

		String deepLCode = context.underTest.toDeepLCode(Language.DE);

		assertThat(deepLCode).isEqualTo("DE");
	}

	private static final class TestContext {

		private final DeepLLanguageMapper underTest = new DeepLLanguageMapper();

	}

}
