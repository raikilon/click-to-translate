package ch.clicktotranslate.segment.domain;

import org.junit.jupiter.api.Test;

import ch.clicktotranslate.segment.infrastructure.TextTranslatorBridge;
import ch.clicktotranslate.translation.infrastructure.LanguageDto;
import ch.clicktotranslate.translation.infrastructure.TextTranslationBridgeController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class SegmentTranslationTest {

	@Test
	void givenBothTranslationsSucceed_whenTranslate_thenReturnsTranslatedSegment() {
		TestContext context = new TestContext();
		Segment givenSegment = context.segment();
		context.givenWordTranslationSucceeds();
		context.givenSentenceTranslationSucceeds();

		TranslatedSegment translatedSegment = context.underTest.translate(givenSegment);

		assertThat(translatedSegment).isEqualTo(new TranslatedSegment(context.word, context.sentence,
				context.expectedWordTranslation, context.expectedSentenceTranslation));
		context.verifyTranslationFor(context.word);
		context.verifyTranslationFor(context.sentence);
		verifyNoMoreInteractions(context.textTranslation);
	}

	@Test
	void givenSentenceTranslationFails_whenTranslate_thenReturnsEmptySentence() {
		TestContext context = new TestContext();
		Segment givenSegment = context.segment();
		context.givenWordTranslationSucceeds();
		context.givenSentenceTranslationFails();

		TranslatedSegment translatedSegment = context.underTest.translate(givenSegment);

		assertThat(translatedSegment).isEqualTo(new TranslatedSegment(context.word, context.sentence,
				context.expectedWordTranslation, context.expectedSentenceTranslation));
		context.verifyTranslationFor(context.word);
		context.verifyTranslationFor(context.sentence);
		verifyNoMoreInteractions(context.textTranslation);
	}

	@Test
	void givenBothTranslationsFail_whenTranslate_thenReturnsEmptyWordAndSentence() {
		TestContext context = new TestContext();
		Segment givenSegment = context.segment();
		context.givenWordTranslationFails();
		context.givenSentenceTranslationFails();

		TranslatedSegment translatedSegment = context.underTest.translate(givenSegment);

		assertThat(translatedSegment).isEqualTo(new TranslatedSegment(context.word, context.sentence,
				context.expectedWordTranslation, context.expectedSentenceTranslation));
		context.verifyTranslationFor(context.word);
		context.verifyTranslationFor(context.sentence);
		verifyNoMoreInteractions(context.textTranslation);
	}

	private final class TestContext {

		private final TextTranslationBridgeController textTranslation = mock(TextTranslationBridgeController.class);

		private final SegmentTranslation underTest = new SegmentTranslation(new TextTranslatorBridge(textTranslation));

		private final String sourceLanguage = "DE";

		private final String targetLanguage = "EN";

		private final String word = "Haus";

		private final String sentence = "Das Haus ist gross.";

		private final String translatedWord = "House";

		private final String translatedSentence = "The house is big.";

		private final String emptyTranslation = "";

		private final String blankTranslation = " ";

		private String expectedWordTranslation;

		private String expectedSentenceTranslation;

		private Segment segment() {
			return new Segment(word, sentence, sourceLanguage, targetLanguage);
		}

		private void givenWordTranslationSucceeds() {
			expectedWordTranslation = translatedWord;
			stubTranslation(word, translatedWord);
		}

		private void givenSentenceTranslationSucceeds() {
			expectedSentenceTranslation = translatedSentence;
			stubTranslation(sentence, translatedSentence);
		}

		private void givenWordTranslationFails() {
			expectedWordTranslation = emptyTranslation;
			stubTranslation(word, emptyTranslation);
		}

		private void givenSentenceTranslationFails() {
			expectedSentenceTranslation = emptyTranslation;
			stubTranslation(sentence, blankTranslation);
		}

		private void stubTranslation(String text, String translatedText) {
			given(textTranslation.translate(argThat(request -> request != null && request.text().equals(text)
					&& request.sourceLanguage().equals(LanguageDto.fromString(sourceLanguage))
					&& request.targetLanguage().equals(LanguageDto.fromString(targetLanguage)))))
				.willReturn(translatedText);
		}

		private void verifyTranslationFor(String text) {
			verify(textTranslation).translate(argThat(request -> request != null && request.text().equals(text)
					&& request.sourceLanguage().equals(LanguageDto.fromString(sourceLanguage))
					&& request.targetLanguage().equals(LanguageDto.fromString(targetLanguage))));
		}

	}

}
