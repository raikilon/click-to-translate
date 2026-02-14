package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import ch.clicktotranslate.vocabulary.domain.Language;
import ch.clicktotranslate.vocabulary.domain.VocabularyRepository;
import ch.clicktotranslate.vocabulary.domain.Lexeme;
import ch.clicktotranslate.vocabulary.domain.SurfaceForm;
import ch.clicktotranslate.vocabulary.domain.TextSpan;
import ch.clicktotranslate.vocabulary.domain.Usage;
import ch.clicktotranslate.vocabulary.domain.UserLexeme;
import ch.clicktotranslate.vocabulary.domain.UserLexemeTranslation;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class JpaVocabularyRepository implements VocabularyRepository {

	private final SpringDataLexemeRepository lexemeRepository;

	private final SpringDataUserLexemeRepository userLexemeRepository;

	private final SpringDataSurfaceFormRepository surfaceFormRepository;

	private final SpringDataUsageRepository usageRepository;

	private final SpringDataUserLexemeTranslationRepository translationRepository;

	public JpaVocabularyRepository(SpringDataLexemeRepository lexemeRepository,
			SpringDataUserLexemeRepository userLexemeRepository, SpringDataSurfaceFormRepository surfaceFormRepository,
			SpringDataUsageRepository usageRepository, SpringDataUserLexemeTranslationRepository translationRepository) {
		this.lexemeRepository = lexemeRepository;
		this.userLexemeRepository = userLexemeRepository;
		this.surfaceFormRepository = surfaceFormRepository;
		this.usageRepository = usageRepository;
		this.translationRepository = translationRepository;
	}

	@Override
	public Lexeme findOrCreateLexeme(Language language, String lemma) {
		String normalized = lemma.trim().toLowerCase(Locale.ROOT);
		return lexemeRepository.findByLanguageAndLemma(language.name(), normalized)
			.map(this::toDomainLexeme)
			.orElseGet(() -> {
				JpaLexemeEntity entity = new JpaLexemeEntity();
				entity.setLanguage(language.name());
				entity.setLemma(normalized);
				return toDomainLexeme(lexemeRepository.save(entity));
			});
	}

	@Override
	public UserLexeme findOrCreateUserLexeme(String userId, Long lexemeId) {
		return userLexemeRepository.findByUserIdAndLexemeId(userId, lexemeId)
			.map(this::toDomainUserLexeme)
			.orElseGet(() -> {
				JpaLexemeEntity lexeme = lexemeRepository.findById(lexemeId)
					.orElseThrow(() -> new IllegalArgumentException("Lexeme not found: " + lexemeId));
				JpaUserLexemeEntity entity = new JpaUserLexemeEntity();
				entity.setUserId(userId);
				entity.setLexeme(lexeme);
				return toDomainUserLexeme(userLexemeRepository.save(entity));
			});
	}

	@Override
	public SurfaceForm findOrCreateSurfaceForm(Long lexemeId, String form) {
		String normalized = form.trim();
		return surfaceFormRepository.findByLexemeIdAndForm(lexemeId, normalized)
			.map(this::toDomainSurfaceForm)
			.orElseGet(() -> {
				JpaLexemeEntity lexeme = lexemeRepository.findById(lexemeId)
					.orElseThrow(() -> new IllegalArgumentException("Lexeme not found: " + lexemeId));
				JpaSurfaceFormEntity entity = new JpaSurfaceFormEntity();
				entity.setLexeme(lexeme);
				entity.setForm(normalized);
				return toDomainSurfaceForm(surfaceFormRepository.save(entity));
			});
	}

	@Override
	public Usage saveUsage(Usage usage) {
		JpaUserLexemeEntity userLexeme = userLexemeRepository.findById(usage.userLexemeId())
			.orElseThrow(() -> new IllegalArgumentException("UserLexeme not found: " + usage.userLexemeId()));
		JpaSurfaceFormEntity surfaceForm = surfaceFormRepository.findById(usage.surfaceFormId())
			.orElseThrow(() -> new IllegalArgumentException("SurfaceForm not found: " + usage.surfaceFormId()));

		JpaUsageEntity entity = new JpaUsageEntity();
		entity.setId(usage.id());
		entity.setUserLexeme(userLexeme);
		entity.setSurfaceForm(surfaceForm);
		entity.setSentence(usage.sentence());
		entity.setTargetLanguage(usage.targetLanguage().name());
		entity.setTranslatedSentence(usage.translatedSentence());
		entity.setSourceStart(usage.sourceStart());
		entity.setSourceEnd(usage.sourceEnd());
		entity.setTranslatedStart(usage.translatedStart());
		entity.setTranslatedEnd(usage.translatedEnd());
		entity.setTranslatedToken(usage.translatedToken());

		return toDomainUsage(usageRepository.save(entity));
	}

	@Override
	public Optional<Usage> findUsageByIdForUser(String userId, Long usageId) {
		return usageRepository.findByIdAndUserLexemeUserId(usageId, userId).map(this::toDomainUsage);
	}

	@Override
	public List<Usage> findUsagesByUserLexemeId(String userId, Long userLexemeId) {
		return usageRepository.findAllByUserLexemeIdAndUserLexemeUserIdOrderByIdAsc(userLexemeId, userId)
			.stream()
			.map(this::toDomainUsage)
			.toList();
	}

	@Override
	public void deleteUsageByIdForUser(String userId, Long usageId) {
		findUsageByIdForUser(userId, usageId).ifPresent(usage -> usageRepository.deleteById(usage.id()));
	}

	@Override
	public Optional<UserLexeme> findUserLexemeByIdForUser(String userId, Long userLexemeId) {
		return userLexemeRepository.findByIdAndUserId(userLexemeId, userId).map(this::toDomainUserLexeme);
	}

	@Override
	public Optional<Lexeme> findLexemeById(Long lexemeId) {
		return lexemeRepository.findById(lexemeId).map(this::toDomainLexeme);
	}

	@Override
	public List<UserLexeme> findUserLexemes(String userId, Optional<Language> sourceLanguage) {
		if (sourceLanguage.isPresent()) {
			return userLexemeRepository.findAllByUserIdAndLexemeLanguage(userId, sourceLanguage.get().name())
				.stream()
				.map(this::toDomainUserLexeme)
				.toList();
		}
		return userLexemeRepository.findAllByUserId(userId).stream().map(this::toDomainUserLexeme).toList();
	}

	@Override
	public List<UserLexeme> searchUserLexemes(String userId, String query) {
		String normalizedQuery = "%" + query.toLowerCase(Locale.ROOT).trim() + "%";
		return userLexemeRepository.searchByUserId(userId, normalizedQuery).stream().map(this::toDomainUserLexeme).toList();
	}

	@Override
	public List<SurfaceForm> findSurfaceFormsByUserLexemeId(String userId, Long userLexemeId) {
		return userLexemeRepository.findByIdAndUserId(userLexemeId, userId)
			.map(userLexeme -> surfaceFormRepository.findAllByLexemeIdOrderByFormAsc(userLexeme.getLexeme().getId())
				.stream()
				.map(this::toDomainSurfaceForm)
				.toList())
			.orElse(List.of());
	}

	@Override
	public Optional<String> findDefaultTranslatedTokenByUserLexemeId(String userId, Long userLexemeId) {
		return usageRepository.findFirstByUserLexemeIdAndUserLexemeUserIdOrderByIdAsc(userLexemeId, userId)
			.map(JpaUsageEntity::getTranslatedToken);
	}

	@Override
	public void deleteUserLexemeByIdForUser(String userId, Long userLexemeId) {
		userLexemeRepository.findByIdAndUserId(userLexemeId, userId).ifPresent(entity -> {
			usageRepository.deleteAllByUserLexemeIdAndUserLexemeUserId(entity.getId(), userId);
			userLexemeRepository.deleteById(entity.getId());
		});
	}

	@Override
	public void deleteUserLexemesByUserAndLanguage(String userId, Language sourceLanguage) {
		List<JpaUserLexemeEntity> entities = userLexemeRepository.findAllByUserIdAndLexemeLanguage(userId,
				sourceLanguage.name());
		for (JpaUserLexemeEntity entity : entities) {
			usageRepository.deleteAllByUserLexemeIdAndUserLexemeUserId(entity.getId(), userId);
		}
		userLexemeRepository.deleteAll(entities);
	}

	@Override
	public Optional<UserLexemeTranslation> findAnyTranslationPreference(String userId, Long sourceLexemeId) {
		return translationRepository.findFirstByUserIdAndSourceLexemeIdOrderByIdAsc(userId, sourceLexemeId)
			.map(this::toDomainTranslation);
	}

	@Override
	public Optional<UserLexemeTranslation> findTranslationPreference(String userId, Long sourceLexemeId,
			Language targetLanguage) {
		return translationRepository
			.findFirstByUserIdAndSourceLexemeIdAndTargetLanguage(userId, sourceLexemeId, targetLanguage.name())
			.map(this::toDomainTranslation);
	}

	@Override
	public UserLexemeTranslation saveTranslationPreference(UserLexemeTranslation translation) {
		Optional<JpaUserLexemeTranslationEntity> existing = translationRepository
			.findFirstByUserIdAndSourceLexemeIdAndTargetLanguage(translation.userId(), translation.sourceLexemeId(),
					translation.targetLanguage().name())
			.filter(entity -> entity.getGloss().equals(translation.gloss()));
		if (existing.isPresent()) {
			return toDomainTranslation(existing.get());
		}
		JpaLexemeEntity sourceLexeme = lexemeRepository.findById(translation.sourceLexemeId())
			.orElseThrow(() -> new IllegalArgumentException("Lexeme not found: " + translation.sourceLexemeId()));
		JpaUserLexemeTranslationEntity entity = new JpaUserLexemeTranslationEntity();
		entity.setId(translation.id());
		entity.setUserId(translation.userId());
		entity.setSourceLexeme(sourceLexeme);
		entity.setTargetLanguage(translation.targetLanguage().name());
		entity.setGloss(translation.gloss());
		return toDomainTranslation(translationRepository.save(entity));
	}

	private Lexeme toDomainLexeme(JpaLexemeEntity entity) {
		return new Lexeme(entity.getId(), Language.valueOf(entity.getLanguage()), entity.getLemma());
	}

	private UserLexeme toDomainUserLexeme(JpaUserLexemeEntity entity) {
		return new UserLexeme(entity.getId(), entity.getLexeme().getId(), entity.getUserId());
	}

	private SurfaceForm toDomainSurfaceForm(JpaSurfaceFormEntity entity) {
		return new SurfaceForm(entity.getId(), entity.getLexeme().getId(), entity.getForm());
	}

	private Usage toDomainUsage(JpaUsageEntity entity) {
		return new Usage(entity.getId(), entity.getUserLexeme().getId(), entity.getSurfaceForm().getId(),
				entity.getSentence(), Language.valueOf(entity.getTargetLanguage()), entity.getTranslatedSentence(),
				new TextSpan(entity.getSourceStart(), entity.getSourceEnd()),
				new TextSpan(entity.getTranslatedStart(), entity.getTranslatedEnd()), entity.getTranslatedToken());
	}

	private UserLexemeTranslation toDomainTranslation(JpaUserLexemeTranslationEntity entity) {
		return new UserLexemeTranslation(entity.getId(), entity.getUserId(), entity.getSourceLexeme().getId(),
				Language.valueOf(entity.getTargetLanguage()), entity.getGloss());
	}

}
