package ch.clicktotranslate.vocabulary.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsageTest {

	@Test
	void givenExactTranslationMatch_whenCreatingUsage_thenSetsExactTranslationSpan() {
		TestContext context = new TestContext();

		context.whenUsageIsCreated(context.exactTranslation, context.wordTranslation);

		assertThat(context.underTest.translationSpan()).isEqualTo(new TextSpan(4, 9));
	}

	@Test
	void givenNoExactMatchAndSingleCloseCandidate_whenCreatingUsage_thenSetsFuzzyTranslationSpan() {
		TestContext context = new TestContext();

		context.whenUsageIsCreated(context.fuzzyTranslation, context.wordTranslation);

		assertThat(context.underTest.translationSpan()).isEqualTo(new TextSpan(4, 9));
	}

	@Test
	void givenNoCandidateWithinThreshold_whenCreatingUsage_thenLeavesTranslationSpanNull() {
		TestContext context = new TestContext();

		context.whenUsageIsCreated(context.unrelatedTranslation, context.wordTranslation);

		assertThat(context.underTest.translationSpan()).isNull();
	}

	@Test
	void givenAmbiguousBestCandidates_whenCreatingUsage_thenLeavesTranslationSpanNull() {
		TestContext context = new TestContext();

		context.whenUsageIsCreated(context.ambiguousTranslation, context.wordTranslation);

		assertThat(context.underTest.translationSpan()).isNull();
	}

	private static final class TestContext {

		private final String sentence = "Das Haus ist gross.";

		private final String word = "Haus";

		private final Language language = Language.EN;

		private final String wordTranslation = "house";

		private final String exactTranslation = "The house is big.";

		private final String fuzzyTranslation = "The hause is big.";

		private final String unrelatedTranslation = "The river is calm.";

		private final String ambiguousTranslation = "A hause and houze nearby.";

		private Usage underTest;

		private void whenUsageIsCreated(String translation, String usageWordTranslation) {
			underTest = new Usage(sentence, word, translation, usageWordTranslation, language);
		}

	}

}
