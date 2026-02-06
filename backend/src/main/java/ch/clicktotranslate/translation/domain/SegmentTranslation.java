package ch.clicktotranslate.translation.domain;

import org.jmolecules.ddd.annotation.Service;

import java.util.concurrent.StructuredTaskScope;

@Service
public class SegmentTranslation {

	private final TextTranslation textTranslation;

	public SegmentTranslation(TextTranslation textTranslation) {
		this.textTranslation = textTranslation;
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

	private String translateText(String text, Language sourceLanguage, Language targetLanguage) {
		if (text == null || text.isBlank()) {
			return null;
		}

		TextToTranslate request = new TextToTranslate(text, sourceLanguage, targetLanguage);

		String translatedText = textTranslation.translate(request);

		if (translatedText == null || translatedText.isBlank()) {
			throw new IllegalStateException("Translation service returned no result.");
		}

		return translatedText;
	}

}
