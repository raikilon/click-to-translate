package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import ch.clicktotranslate.vocabulary.application.EntryQuery;
import ch.clicktotranslate.vocabulary.application.PageRequest;
import ch.clicktotranslate.vocabulary.application.PageResult;
import ch.clicktotranslate.vocabulary.application.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.Entry;
import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.Term;
import ch.clicktotranslate.vocabulary.domain.UserId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

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
		return entryRepository
			.findWithUsagesByUserIdAndLanguageAndTerm(userId.value(), term.language().name(), term.term())
			.map(mapper::toDomainEntry);
	}

	@Override
	public PageResult<Entry> findEntriesByUser(UserId userId, PageRequest pageRequest) {
		Page<EntryDataProjection> page = entryRepository.findEntryDataByUserId(userId.value(),
				mapper.toSpringPageable(pageRequest));
		List<Entry> items = toEntries(page.getContent());
		return new PageResult<>(items, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(),
				page.hasNext());
	}

	@Override
	public PageResult<Entry> findByLanguage(UserId userId, Language sourceLanguage, PageRequest pageRequest) {
		Page<EntryDataProjection> page = entryRepository.findEntryDataByUserIdAndLanguage(userId.value(),
				sourceLanguage.name(), mapper.toSpringPageable(pageRequest));
		List<Entry> items = toEntries(page.getContent());
		return new PageResult<>(items, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(),
				page.hasNext());
	}

	@Override
	public PageResult<Entry> search(UserId userId, String query, PageRequest pageRequest) {
		Page<EntryDataProjection> page = entryRepository.findEntryDataByUserIdAndTermContainingIgnoreCase(
				userId.value(), query.trim(), mapper.toSpringPageable(pageRequest));
		List<Entry> items = toEntries(page.getContent());
		return new PageResult<>(items, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(),
				page.hasNext());
	}

	@Override
	public void saveEntry(Entry entry) {
		entryRepository.save(mapper.toJpaEntryEntity(entry));
	}

	@Override
	public Optional<Entry> findEntryById(UserId userId, Entry.Id entryId) {
		return entryRepository.findWithUsagesByIdAndUserId(entryId.value(), userId.value()).map(mapper::toDomainEntry);
	}

	@Override
	public boolean existsEntryById(UserId userId, Entry.Id entryId) {
		return entryRepository.existsByIdAndUserId(entryId.value(), userId.value());
	}

	@Override
	public PageResult<ch.clicktotranslate.vocabulary.domain.Usage> findUsagesByEntry(UserId userId, Entry.Id entryId,
			PageRequest pageRequest) {
		Page<JpaUsageEntity> page = usageRepository.findByEntryIdAndEntryUserId(entryId.value(), userId.value(),
				mapper.toSpringPageable(pageRequest));
		List<ch.clicktotranslate.vocabulary.domain.Usage> items = page.getContent()
			.stream()
			.map(mapper::toDomainUsage)
			.toList();
		return new PageResult<>(items, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(),
				page.hasNext());
	}

	@Override
	public void deleteEntryById(UserId userId, Entry.Id entryId) {
		entryRepository.findByIdAndUserId(entryId.value(), userId.value()).ifPresent(entryRepository::delete);
	}

	private List<Entry> toEntries(List<EntryDataProjection> projections) {
		return projections.stream()
			.map(entry -> mapper.toDomainEntry(entry, usageRepository.findFirstByEntryIdOrderByIdDesc(entry.getId())))
			.toList();
	}

}
