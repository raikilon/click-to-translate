package ch.clicktotranslate.segment.application;

import ch.clicktotranslate.segment.domain.Segment;
import org.jmolecules.ddd.annotation.Service;

import java.util.concurrent.StructuredTaskScope;

@Service
public class SegmentTranslatorService {

	private final TextTranslator textTranslator;

	public SegmentTranslatorService(TextTranslator textTranslator) {
		this.textTranslator = textTranslator;
	}

	public Segment translate(Segment segment) {
		try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAll())) {
			var translateWordTask = scope.fork(() -> translateText(segment.word(), segment.sourceLanguage(),
					segment.targetLanguage(), segment.sentence()));
			var translateSentenceTask = scope.fork(
					() -> translateText(segment.sentence(), segment.sourceLanguage(), segment.targetLanguage(), null));

			scope.join();

			String translatedWord = this.resultOrNull(translateWordTask);
			String translatedSentence = this.resultOrNull(translateSentenceTask);
			return segment.withTranslations(translatedWord, translatedSentence);

		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("Translation interrupted.", e);
		}
	}

	private String resultOrNull(StructuredTaskScope.Subtask<String> task) {
		if (task.state() == StructuredTaskScope.Subtask.State.SUCCESS) {
			return task.get();
		}
		return null;
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
