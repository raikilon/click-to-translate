package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import ch.clicktotranslate.vocabulary.application.EntryData;
import ch.clicktotranslate.vocabulary.application.EntryQuery;
import ch.clicktotranslate.vocabulary.application.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.Term;
import ch.clicktotranslate.vocabulary.domain.UserId;
import ch.clicktotranslate.vocabulary.domain.Entry;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class JpaVocabularyRepository implements VocabularyRepository, EntryQuery {

	private final SpringDataEntryRepository entryRepository;

	private final VocabularyJpaMapper mapper;

	public JpaVocabularyRepository(SpringDataEntryRepository entryRepository) {
		this.entryRepository = entryRepository;
		this.mapper = new VocabularyJpaMapper();
	}

	@Override
	public Optional<Entry> findEntryByTerm(UserId userId, Term term) {
		return entryRepository
			.findWithUsagesByUserIdAndSourceLanguageAndSourceLemma(userId.value(), term.language().name(),
					term.term())
			.map(mapper::toDomainEntry);
	}

	@Override
	public List<EntryData> findAll(UserId userId) {
		return entryRepository.findAllByUserIdOrderByIdAsc(userId.value()).stream().map(mapper::toEntry).toList();
	}

	@Override
	public List<EntryData> findByLanguage(UserId userId, Language sourceLanguage) {
		return entryRepository.findAllByUserIdAndSourceLanguageOrderByIdAsc(userId.value(), sourceLanguage.name())
			.stream()
			.map(mapper::toEntry)
			.toList();
	}

	@Override
	public List<EntryData> search(UserId userId, String query) {
		return entryRepository.findAllByUserIdAndSourceLemmaContainingIgnoreCaseOrderByIdAsc(userId.value(),
				query.trim()).stream().map(mapper::toEntry).toList();
	}

	@Override
	public void saveEntry(Entry entry) {
		mapper.toDomainEntry(entryRepository.save(mapper.toJpaEntryEntity(entry)));
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

}


