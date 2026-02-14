package ch.clicktotranslate.vocabulary.application;

public class EntryNotFoundException extends RuntimeException {

	public EntryNotFoundException() {
		super("Vocabulary entry not found");
	}

}
