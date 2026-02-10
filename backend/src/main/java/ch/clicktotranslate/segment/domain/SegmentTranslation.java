package ch.clicktotranslate.segment.domain;

import org.jmolecules.ddd.annotation.Service;

import java.util.concurrent.StructuredTaskScope;

@Service
public class SegmentTranslation {

	private final TextTranslator textTranslator;

	public SegmentTranslation(TextTranslator textTranslator) {
		this.textTranslator = textTranslator;
	}

	public TranslatedSegment translate(Segment segment) {
		try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAll())) {
			var translateWordTask = scope
				.fork(() -> translateText(segment.word(), segment.sourceLanguage(), segment.targetLanguage()));
			var translateSentenceTask = scope
				.fork(() -> translateText(segment.sentence(), segment.sourceLanguage(), segment.targetLanguage()));

			scope.join();

			String translatedWord = this.resultOrEmpty(translateWordTask);
			String translatedSentence = this.resultOrEmpty(translateSentenceTask);

			return new TranslatedSegment(segment.word(), segment.sentence(), translatedWord, translatedSentence);

		}
		catch (InterruptedException e) {
			return new TranslatedSegment(segment.word(), segment.sentence(), "", "");
		}
	}

	private String resultOrEmpty(StructuredTaskScope.Subtask<String> task) {
		if (task.state() == StructuredTaskScope.Subtask.State.SUCCESS) {
			return task.get();
		}

		return "";
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
