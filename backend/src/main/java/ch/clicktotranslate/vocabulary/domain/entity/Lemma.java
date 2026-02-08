package ch.clicktotranslate.vocabulary.domain.entity;

import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;
import java.util.ArrayList;
import java.util.List;
import ch.clicktotranslate.vocabulary.domain.Language;

@AggregateRoot
@Entity
public class Lemma {

	@Identity
	private Long id;

	private String userId;

	private String lemma;

	private String lemmaTranslation;

	private Language sourceLanguage;

	private Language targetLanguage;

	private List<Usage> usages;

	public Lemma(Long id, String userId, String lemma, String lemmaTranslation, Language sourceLanguage,
			Language targetLanguage, List<Usage> usages) {
		this.id = id;
		this.userId = userId;
		this.lemma = lemma;
		this.lemmaTranslation = lemmaTranslation;
		this.sourceLanguage = sourceLanguage;
		this.targetLanguage = targetLanguage;
		this.usages = usages == null ? new ArrayList<>() : new ArrayList<>(usages);
	}

	public Lemma(String userId, String lemma, String lemmaTranslation, Language sourceLanguage, Language targetLanguage,
			List<Usage> usages) {
		this(null, userId, lemma, lemmaTranslation, sourceLanguage, targetLanguage, usages);
	}

	public Long id() {
		return id;
	}

	public String userId() {
		return userId;
	}

	public String lemma() {
		return lemma;
	}

	public String lemmaTranslation() {
		return lemmaTranslation;
	}

	public Language sourceLanguage() {
		return sourceLanguage;
	}

	public Language targetLanguage() {
		return targetLanguage;
	}

	public List<Usage> usages() {
		return usages;
	}

	public void updateLemma(String lemma, String lemmaTranslation) {
		this.lemma = lemma;
		this.lemmaTranslation = lemmaTranslation;
	}

	public void addUsage(Usage usage) {
		this.usages.add(usage);
	}

	public boolean replaceUsage(Usage usage) {
		for (int index = 0; index < usages.size(); index++) {
			if (usages.get(index).id().equals(usage.id())) {
				usages.set(index, usage);
				return true;
			}
		}
		return false;
	}

	public boolean removeUsage(Long usageId) {
		return usages.removeIf(usage -> usage.id().equals(usageId));
	}

}
