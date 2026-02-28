package ch.clicktotranslate.vocabulary.domain;

import ch.clicktotranslate.auth.UserId;
import java.util.List;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.ddd.types.ValueObject;

public class Entry implements AggregateRoot<Entry, Entry.Id> {

	private static final int MAX_USAGES = 20;

	private final Id id;

	private final UserId userId;

	private final Term term;

	private String termCustomization;

	private final List<Term> translations;

	private final List<Usage> usages;

	private final Instant lastEdit;

	private final Instant createdAt;

	public Entry(Id id, UserId userId, Term term, String termCustomization, List<Term> translations, List<Usage> usages,
			Instant lastEdit, Instant createdAt) {
		this.id = id;
		this.userId = requireUserId(userId);
		this.term = requireTerm(term);
		this.termCustomization = termCustomization;
		this.translations = requireTranslations(translations);
		this.usages = requireUsages(usages);
		if (lastEdit == null) {
			throw new IllegalArgumentException("lastEdit must not be null");
		}
		if (createdAt == null) {
			throw new IllegalArgumentException("createdAt must not be null");
		}
		this.lastEdit = lastEdit;
		this.createdAt = createdAt;
	}

	public static Entry createNew(UserId userId, Term term) {
		Instant now = Instant.now();
		return new Entry(null, userId, term, null, List.of(), List.of(), now, now);
	}

	@Override
	public Id getId() {
		return id;
	}

	public Id id() {
		return id;
	}

	public UserId userId() {
		return userId;
	}

	public Term term() {
		return term;
	}

	public Optional<String> termCustomization() {
		return Optional.ofNullable(termCustomization);
	}

	public List<Term> translations() {
		return List.copyOf(translations);
	}

	public List<Usage> usages() {
		return List.copyOf(usages);
	}

	public Instant lastEdit() {
		return lastEdit;
	}

	public Instant createdAt() {
		return createdAt;
	}

	public void updateTerm(String customization) {
		this.termCustomization = requireCustomization(customization);
	}

	public void setTranslation(Language language, String term) {
		Language requiredLanguage = requireLanguage(language);
		Term newTranslation = new Term(requiredLanguage, requireTermValue(term));
		translations.removeIf(translation -> translation.language() == requiredLanguage);
		translations.add(newTranslation);
	}

	public boolean addTranslation(Language language, String term) {
		Language requiredLanguage = requireLanguage(language);
		if (translations.stream().anyMatch(translation -> translation.language() == requiredLanguage)) {
			return false;
		}
		translations.add(new Term(requiredLanguage, requireTermValue(term)));
		return true;
	}

	public boolean addUsage(Usage usage) {
		Usage requiredUsage = requireUsage(usage);
		if (requiredUsage.id() != null) {
			usages.removeIf(existing -> Objects.equals(existing.id(), requiredUsage.id()));
		}
		if (usages.stream().anyMatch(existing -> isSameUsage(existing, requiredUsage))) {
			return false;
		}
		if (usages.size() >= MAX_USAGES) {
			int removableIndex = findOldestNonStarredUsageIndex();
			if (removableIndex < 0) {
				return false;
			}
			usages.remove(removableIndex);
		}
		usages.add(requiredUsage);
		return true;
	}

	public void removeUsage(Usage.Id usageId) {
		Usage.Id requiredUsageId = requireUsageId(usageId);
		int usageIndex = findUsageIndex(requiredUsageId);
		if (usageIndex < 0) {
			throw new IllegalArgumentException("usageId must be part of usages");
		}
		if (usages.get(usageIndex).starred()) {
			throw new IllegalArgumentException("starred usage cannot be deleted");
		}
		usages.remove(usageIndex);
	}

	public void starUsage(Usage.Id usageId) {
		Usage.Id requiredUsageId = requireUsageId(usageId);
		int usageIndex = findUsageIndex(requiredUsageId);
		if (usageIndex < 0) {
			throw new IllegalArgumentException("usageId must be part of usages");
		}
		usages.get(usageIndex).star();
	}

	private static UserId requireUserId(UserId value) {
		if (value == null) {
			throw new IllegalArgumentException("userId must not be null");
		}
		return value;
	}

	private static Term requireTerm(Term value) {
		if (value == null) {
			throw new IllegalArgumentException("term must not be null");
		}
		return value;
	}

	private static String requireCustomization(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("termCustomization must not be null");
		}
		return value.trim();
	}

	private static List<Term> requireTranslations(List<Term> value) {
		if (value == null) {
			throw new IllegalArgumentException("translations must not be null");
		}
		return new ArrayList<>(value.stream().map(Entry::requireTerm).toList());
	}

	private static Usage.Id requireUsageId(Usage.Id value) {
		if (value == null) {
			throw new IllegalArgumentException("usageId must not be null");
		}
		return value;
	}

	private static List<Usage> requireUsages(List<Usage> value) {
		if (value == null) {
			throw new IllegalArgumentException("usages must not be null");
		}
		return new ArrayList<>(value.stream().map(Entry::requireUsage).toList());
	}

	private static Usage requireUsage(Usage value) {
		if (value == null) {
			throw new IllegalArgumentException("usage must not be null");
		}
		return value;
	}

	private static Language requireLanguage(Language value) {
		if (value == null) {
			throw new IllegalArgumentException("language must not be null");
		}
		return value;
	}

	private static String requireTermValue(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("term must not be blank");
		}
		return value.trim();
	}

	private int findUsageIndex(Usage.Id usageId) {
		for (int i = 0; i < usages.size(); i++) {
			if (Objects.equals(usages.get(i).id(), usageId)) {
				return i;
			}
		}
		return -1;
	}

	private int findOldestNonStarredUsageIndex() {
		int oldest = -1;

		for (int i = 0; i < usages.size(); i++) {
			Usage usage = usages.get(i);
			if (usage.starred())
				continue;

			if (oldest < 0 || usage.createdAt().isBefore(usages.get(oldest).createdAt())) {
				oldest = i;
			}
		}
		return oldest;
	}

	private static boolean isSameUsage(Usage existing, Usage candidate) {
		return existing.sentence().equals(candidate.sentence()) && existing.language().equals(candidate.language());
	}

	public record Id(Long value) implements Identifier, ValueObject {

		public static Id of(Long value) {
			return new Id(value);
		}

		public Id {
			if (value == null || value <= 0) {
				throw new IllegalArgumentException("entryId must be > 0");
			}
		}

	}

}
