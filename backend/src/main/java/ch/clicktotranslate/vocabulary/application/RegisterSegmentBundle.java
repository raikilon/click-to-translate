package ch.clicktotranslate.vocabulary.application;

import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.Lexeme;
import ch.clicktotranslate.vocabulary.domain.SegmentBundle;
import ch.clicktotranslate.vocabulary.domain.SurfaceForm;
import ch.clicktotranslate.vocabulary.domain.TextSpan;
import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.UserLexeme;
import java.util.Optional;

public class RegisterSegmentBundle {

	private final VocabularyRepository vocabularyRepository;

	public RegisterSegmentBundle(VocabularyRepository vocabularyRepository) {
		this.vocabularyRepository = vocabularyRepository;
	}

	public void execute(SegmentBundle event) {
		Lexeme lexeme = vocabularyRepository.findOrCreateLexeme(event.sourceLanguage(), event.tokenizedWord());
		UserLexeme userLexeme = vocabularyRepository.findOrCreateUserLexeme(event.userId(), lexeme.id());
		SurfaceForm surfaceForm = vocabularyRepository.findOrCreateSurfaceForm(lexeme.id(), event.word());

		TextSpan sourceSpan = findSpan(event.sentence(), event.word(), event.tokenizedWord(), "source");
		TextSpan translatedSpan = findSpan(event.sentenceTranslation(), event.wordTranslation(),
				event.tokenizedWordTranslation(), "translated");
		String translatedToken = event.sentenceTranslation().substring(translatedSpan.start(), translatedSpan.end()).trim();

		Usage usage = new Usage(userLexeme.id(), surfaceForm.id(), event.sentence(), event.targetLanguage(),
				event.sentenceTranslation(), sourceSpan, translatedSpan, translatedToken);
		vocabularyRepository.saveUsage(usage);
	}

	private TextSpan findSpan(String sentence, String primaryToken, String fallbackToken, String fieldName) {
		Optional<TextSpan> primary = TextSpan.find(sentence, primaryToken);
		if (primary.isPresent()) {
			return primary.get();
		}
		return TextSpan.find(sentence, fallbackToken)
			.orElseThrow(() -> new IllegalArgumentException(fieldName + " token not found in sentence"));
	}

}
