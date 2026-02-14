package ch.clicktotranslate.tokenizer.application;

import ch.clicktotranslate.tokenizer.domain.SegmentBundleTokenizedEvent;
import org.jmolecules.ddd.annotation.Service;

@Service
public class WordTokenizer {

	private final Tokenizer tokenizer;

	private final TextTranslator textTranslator;

	private final SegmentBundleTokenizedEventPublisher publisher;

	public WordTokenizer(Tokenizer tokenizer, TextTranslator textTranslator,
			SegmentBundleTokenizedEventPublisher publisher) {
		this.tokenizer = tokenizer;
		this.textTranslator = textTranslator;
		this.publisher = publisher;
	}

	public void tokenize(TranslatedSegmentBundle segmentBundle) {
		String tokenizedWord = tokenizer.tokenize(segmentBundle.word());
		String tokenizedWordTranslation = translateText(tokenizedWord, segmentBundle.sourceLanguage(),
				segmentBundle.targetLanguage());

		SegmentBundleTokenizedEvent event = new SegmentBundleTokenizedEvent(segmentBundle.userId(), tokenizedWord,
				tokenizedWordTranslation, segmentBundle.sentence(), segmentBundle.sentenceTranslation(),
				segmentBundle.word(), segmentBundle.wordTranslation(), segmentBundle.sourceLanguage(),
				segmentBundle.targetLanguage(), segmentBundle.occurredAt());

		this.publisher.publish(event);
	}

	private String translateText(String text, String sourceLanguage, String targetLanguage) {
		if (text == null || text.isBlank()) {
			return null;
		}

		String translatedText = textTranslator.translate(text, sourceLanguage, targetLanguage);

		if (translatedText == null || translatedText.isBlank()) {
			throw new IllegalStateException("Translation service returned no result.");
		}

		return translatedText;
	}

}
