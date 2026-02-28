package ch.clicktotranslate.lemmatizer.application;

import ch.clicktotranslate.lemmatizer.domain.SegmentBundleLemmatizedEvent;
import org.jmolecules.ddd.annotation.Service;

@Service
public class WordLemmatizer {

	private final Lemmatizer lemmatizer;

	private final TextTranslator textTranslator;

	private final SegmentBundleLemmatizedEventPublisher publisher;

	public WordLemmatizer(Lemmatizer lemmatizer, TextTranslator textTranslator,
			SegmentBundleLemmatizedEventPublisher publisher) {
		this.lemmatizer = lemmatizer;
		this.textTranslator = textTranslator;
		this.publisher = publisher;
	}

	public void lemmatize(TranslatedSegmentBundle segmentBundle) {
		String lemmatizedWord = lemmatizer.lemmatize(segmentBundle.word());
		String lemmatizedWordTranslation = translateText(lemmatizedWord, segmentBundle.sourceLanguage(),
				segmentBundle.targetLanguage(), segmentBundle.sentence());

		SegmentBundleLemmatizedEvent event = new SegmentBundleLemmatizedEvent(segmentBundle.userId(), lemmatizedWord,
				lemmatizedWordTranslation, segmentBundle.sentence(), segmentBundle.sentenceTranslation(),
				segmentBundle.word(), segmentBundle.wordTranslation(), segmentBundle.sourceLanguage(),
				segmentBundle.targetLanguage(), segmentBundle.occurredAt());

		this.publisher.publish(event);
	}

	private String translateText(String text, String sourceLanguage, String targetLanguage, String context) {
		if (text == null || text.isBlank()) {
			return null;
		}

		String translatedText = textTranslator.translate(text, sourceLanguage, targetLanguage, context);

		if (translatedText == null || translatedText.isBlank()) {
			throw new IllegalStateException("Translation service returned no result.");
		}

		return translatedText;
	}

}
