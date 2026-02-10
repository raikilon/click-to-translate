package ch.clicktotranslate.segment.application;

import ch.clicktotranslate.segment.domain.Segment;
import ch.clicktotranslate.segment.domain.SegmentTranslation;
import ch.clicktotranslate.segment.domain.TranslatedSegment;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class SegmentBundleTranslationControllerTest {

	@Test
	void givenSegmentBundle_whenTranslate_thenPublishesEventAndReturnsTranslatedSegment() {
		TestContext context = new TestContext();
		SegmentBundle segmentBundle = context.segmentBundle();
		context.givenSegmentBundleMapping(segmentBundle);
		context.givenSegmentTranslation();
		context.givenEventMapping(segmentBundle);

		TranslatedSegment result = context.underTest.translate(segmentBundle);

		assertThat(result).isEqualTo(context.translatedSegment);
		context.verifySegmentBundleMapped(segmentBundle);
		context.verifySegmentTranslated();
		context.verifyEventMapped(segmentBundle);
		context.verifyEventPublished();
		verifyNoMoreInteractions(context.segmentBundleMapper, context.segmentTranslation,
				context.translatedWordEventMapper, context.eventPublisher);
	}

	private final class TestContext {

		private final SegmentTranslation segmentTranslation = mock(SegmentTranslation.class);

		private final EventPublisher eventPublisher = mock(EventPublisher.class);

		private final TranslatedWordEventMapper translatedWordEventMapper = mock(TranslatedWordEventMapper.class);

		private final SegmentBundleMapper segmentBundleMapper = mock(SegmentBundleMapper.class);

		private final SegmentBundleTranslationController underTest = new SegmentBundleTranslationController(
				segmentTranslation, eventPublisher, translatedWordEventMapper, segmentBundleMapper);

		private final String sourceLanguage = "DE";

		private final String targetLanguage = "EN";

		private final Instant occurredAt = Instant.parse("2026-02-07T10:15:30Z");

		private final String userId = "user-1";

		private final String word = "Haus";

		private final String sentence = "Das Haus ist gross.";

		private final String translatedWord = "House";

		private final String translatedSentence = "The house is big.";

		private final String sourceType = "page";

		private final String sourceId = "42";

		private final String sourceTitle = "Readme";

		private final String sourceUrl = "https://example.com";

		private final String sourceDomain = "example.com";

		private final Integer selectionOffset = 3;

		private final Integer paragraphIndex = 1;

		private final SegmentBundle.Source source = new SegmentBundle.Source(sourceType, sourceId, sourceTitle);

		private final SegmentBundle.SourceMetadata sourceMetadata = new SegmentBundle.GenericSourceMetadata(sourceUrl,
				sourceDomain, selectionOffset, paragraphIndex);

		private final Segment segment = new Segment(word, sentence, sourceLanguage, targetLanguage);

		private final TranslatedSegment translatedSegment = new TranslatedSegment(word, sentence, translatedWord,
				translatedSentence);

		private final TranslatedWordEvent event = new TranslatedWordEvent(userId, word, sentence, translatedWord,
				translatedSentence, sourceLanguage, targetLanguage,
				new TranslatedWordEvent.Source(sourceType, sourceId, sourceTitle),
				new TranslatedWordEvent.GenericSourceMetadata(sourceUrl, sourceDomain, selectionOffset, paragraphIndex),
				occurredAt);

		private SegmentBundle segmentBundle() {
			return new SegmentBundle(userId, word, sentence, sourceLanguage, targetLanguage, source, sourceMetadata,
					occurredAt);
		}

		private void givenSegmentBundleMapping(SegmentBundle segmentBundle) {
			given(segmentBundleMapper.map(segmentBundle)).willReturn(segment);
		}

		private void givenSegmentTranslation() {
			given(segmentTranslation.translate(segment)).willReturn(translatedSegment);
		}

		private void givenEventMapping(SegmentBundle segmentBundle) {
			given(translatedWordEventMapper.map(segmentBundle, translatedSegment)).willReturn(event);
		}

		private void verifySegmentBundleMapped(SegmentBundle segmentBundle) {
			verify(segmentBundleMapper).map(segmentBundle);
		}

		private void verifySegmentTranslated() {
			verify(segmentTranslation).translate(segment);
		}

		private void verifyEventMapped(SegmentBundle segmentBundle) {
			verify(translatedWordEventMapper).map(segmentBundle, translatedSegment);
		}

		private void verifyEventPublished() {
			verify(eventPublisher).publish(event);
		}

	}

}
