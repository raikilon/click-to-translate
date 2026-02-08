package ch.clicktotranslate.vocabulary.domain.entity;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

@Entity
public class Usage {

	@Identity
	private Long id;

	private Long lemmaId;

	private String word;

	private String wordTranslation;

	private String usage;

	private String usageTranslation;

	public Usage(Long id, Long lemmaId, String word, String wordTranslation, String usage, String usageTranslation) {
		this.id = id;
		this.lemmaId = lemmaId;
		this.word = word;
		this.wordTranslation = wordTranslation;
		this.usage = usage;
		this.usageTranslation = usageTranslation;
	}

	public Usage(Long lemmaId, String word, String wordTranslation, String usage, String usageTranslation) {
		this(null, lemmaId, word, wordTranslation, usage, usageTranslation);
	}

	public Long id() {
		return id;
	}

	public Long lemmaId() {
		return lemmaId;
	}

	public String word() {
		return word;
	}

	public String wordTranslation() {
		return wordTranslation;
	}

	public String usage() {
		return usage;
	}

	public String usageTranslation() {
		return usageTranslation;
	}

}
