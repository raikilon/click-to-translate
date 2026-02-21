package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import ch.clicktotranslate.vocabulary.application.EntryQuery;
import ch.clicktotranslate.vocabulary.application.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.Entry;
import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.Term;
import ch.clicktotranslate.vocabulary.domain.UserId;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class JpaVocabularyRepository implements VocabularyRepository, EntryQuery {

	private final SpringDataEntryRepository entryRepository;

	private final SpringDataUsageRepository usageRepository;

	private final VocabularyJpaMapper mapper;

	public JpaVocabularyRepository(SpringDataEntryRepository entryRepository,
			SpringDataUsageRepository usageRepository) {
		this.entryRepository = entryRepository;
		this.usageRepository = usageRepository;
		this.mapper = new VocabularyJpaMapper();
	}

	@Override
	public Optional<Entry> findEntryByTerm(UserId userId, Term term) {
		return entryRepository.findWithUsagesByUserIdAndLanguageAndTerm(userId.value(), term.language().name(),
				term.term())
			.map(mapper::toDomainEntry);
	}

	@Override
	public List<Entry> findAll(UserId userId) {
		return toEntries(entryRepository.findEntryDataByUserIdOrderByIdAsc(userId.value()));
	}

	@Override
	public List<Entry> findByLanguage(UserId userId, Language sourceLanguage) {
		return toEntries(entryRepository.findEntryDataByUserIdAndLanguageOrderByIdAsc(userId.value(),
				sourceLanguage.name()));
	}

	@Override
	public List<Entry> search(UserId userId, String query) {
		return toEntries(entryRepository.findEntryDataByUserIdAndTermContainingIgnoreCaseOrderByIdAsc(
				userId.value(), query.trim()));
	}

	@Override
	public void saveEntry(Entry entry) {
		entryRepository.save(mapper.toJpaEntryEntity(entry));
	}

	@Override
	public Optional<Entry> findEntryById(UserId userId, Entry.Id entryId) {
		return entryRepository.findWithUsagesByIdAndUserId(entryId.value(), userId.value())
			.map(mapper::toDomainEntry);
	}

	@Override
	public void deleteEntryById(UserId userId, Entry.Id entryId) {
		entryRepository.findByIdAndUserId(entryId.value(), userId.value()).ifPresent(entryRepository::delete);
	}

	private List<Entry> toEntries(List<EntryDataProjection> projections) {
		return projections.stream().map(entry -> new Entry(Entry.Id.of(entry.getId()),
				UserId.of(entry.getUserId()),
				new Term(Language.valueOf(entry.getLanguage()), entry.getTerm()),
				entry.getTermCustomization().orElse(null),
				toTranslations(entry.getTranslations()),
				usageRepository.findFirstByEntryIdOrderByIdDesc(entry.getId())
					.map(mapper::toDomainUsage)
					.stream()
					.toList(),
				entry.getLastEdit(), entry.getCreatedAt())).toList();
	}

	private List<Term> toTranslations(java.util.Collection<JpaTermTranslation> translations) {
		return translations.stream()
			.map(translation -> new Term(Language.valueOf(translation.getLanguage()), translation.getTerm()))
			.toList();
	}

}


