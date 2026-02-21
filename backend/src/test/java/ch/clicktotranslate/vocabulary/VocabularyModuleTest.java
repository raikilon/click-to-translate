package ch.clicktotranslate.vocabulary;

import ch.clicktotranslate.tokenizer.domain.SegmentBundleTokenizedEvent;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.EntryDataProjection;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.JpaEntryEntity;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.JpaUsageEntity;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataEntryRepository;
import java.time.Instant;
import java.util.List;
import java.util.function.BiConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest
@TestPropertySource(
		properties = { "spring.datasource.url=jdbc:h2:mem:vocabulary-module-test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
				"spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa",
				"spring.datasource.password=", "spring.jpa.hibernate.ddl-auto=create-drop" })
class VocabularyModuleTest {

	@Autowired
	private SpringDataEntryRepository entryRepository;

	@BeforeEach
	void cleanDatabase() {
		entryRepository.deleteAll();
	}

	@Test
	void givenNewSegmentEvent_whenHandled_thenCreatesEntryWithUsage(Scenario scenario) {
		TestContext context = new TestContext();

		scenario.stimulate(publishEvent(context.newSegmentEvent()))
			.andWaitForStateChange(() -> entriesByUser(context.userId()),
					entries -> entries.size() == 1)
			.andVerify(entries -> {
				assertThat(entries).hasSize(1);
				EntryDataProjection entry = entries.getFirst();
				assertThat(entry.getId()).isNotNull();
				assertThat(entry.getUserId()).isEqualTo(context.userId());
				assertThat(entry.getLanguage()).isEqualTo(context.sourceLanguage());
				assertThat(entry.getTerm()).isEqualTo(context.normalizedTokenizedWord());

				JpaEntryEntity persistedEntry = findEntry(context);
				assertThat(persistedEntry.getUsages()).hasSize(1);

				JpaUsageEntity usage = persistedEntry.getUsages().iterator().next();
				assertThat(usage.getSentence()).isEqualTo(context.firstSentence());
				assertThat(usage.getSentenceStart()).isEqualTo(4);
				assertThat(usage.getSentenceEnd()).isEqualTo(8);
				assertThat(usage.getTranslation()).isEqualTo(context.firstSentenceTranslation());
				assertThat(usage.getTranslationStart()).isEqualTo(4);
				assertThat(usage.getTranslationEnd()).isEqualTo(9);
				assertThat(usage.getTargetLanguage()).isEqualTo(context.targetLanguage());
			});
	}

	@Test
	void givenExistingSegmentWithNewUsage_whenHandled_thenAddsOnlyNewUsage(Scenario scenario) {
		TestContext context = new TestContext();

		scenario.stimulate(publishEvent(context.newSegmentEvent()))
			.andWaitForStateChange(() -> usageCount(context), count -> count == 1)
			.andVerify(count -> assertThat(count).isEqualTo(1));

		scenario.stimulate(publishEvent(context.newUsageForExistingSegmentEvent()))
			.andWaitForStateChange(() -> usageCount(context), count -> count == 2)
			.andVerify(count -> {
				assertThat(count).isEqualTo(2);
				JpaEntryEntity entry = findEntry(context);
				assertThat(entry.getUsages()).hasSize(2);
				assertThat(entry.getUsages().stream().map(JpaUsageEntity::getSentence).toList())
					.containsExactlyInAnyOrder(context.firstSentence(), context.secondSentence());
				assertThat(entry.getUsages().stream().map(JpaUsageEntity::getTranslation).toList())
					.containsExactlyInAnyOrder(context.firstSentenceTranslation(), context.secondSentenceTranslation());
			});
	}

	@Test
	void givenExistingSegmentAndExistingUsage_whenHandled_thenDoesNotCreateDuplicateUsage(Scenario scenario) {
		TestContext context = new TestContext();

		scenario.stimulate(publishEvent(context.newSegmentEvent()))
			.andWaitForStateChange(() -> usageCount(context), count -> count == 1)
			.andVerify(count -> assertThat(count).isEqualTo(1));

		scenario.stimulate(publishEvent(context.newSegmentEvent()))
			.andWaitForStateChange(() -> usageCount(context), count -> count == 1)
			.andVerify(count -> {
				assertThat(count).isEqualTo(1);
				assertThat(entriesByUser(context.userId())).hasSize(1);
			});
	}

	private List<EntryDataProjection> entriesByUser(String userId) {
		return entryRepository.findEntryDataByUserId(userId, PageRequest.of(0, 1_000)).getContent();
	}

	private int usageCount(TestContext context) {
		return findEntry(context).getUsages().size();
	}

	private JpaEntryEntity findEntry(TestContext context) {
		return entryRepository
			.findWithUsagesByUserIdAndLanguageAndTerm(context.userId(), context.sourceLanguage(),
					context.normalizedTokenizedWord())
			.orElseThrow();
	}

	private BiConsumer<TransactionOperations, ApplicationEventPublisher> publishEvent(
			SegmentBundleTokenizedEvent event) {
		return (tx, publisher) -> publisher.publishEvent(event);
	}

	private static final class TestContext {

		private final Instant occurredAt = Instant.parse("2026-02-07T10:15:30Z");

		private final String userId = "user-1";

		private final String tokenizedWord = "Haus";

		private final String tokenizedWordTranslation = "house";

		private final String sourceLanguage = "DE";

		private final String targetLanguage = "EN";

		private final String firstSentence = "Das Haus ist gross.";

		private final String firstSentenceTranslation = "The house is big.";

		private final String secondSentence = "Ein Haus am See.";

		private final String secondSentenceTranslation = "A house by the lake.";

		private final String word = "Haus";

		private final String wordTranslation = "house";

		private String userId() {
			return userId;
		}

		private String sourceLanguage() {
			return sourceLanguage;
		}

		private String targetLanguage() {
			return targetLanguage;
		}

		private String normalizedTokenizedWord() {
			return tokenizedWord.toLowerCase();
		}

		private String firstSentence() {
			return firstSentence;
		}

		private String firstSentenceTranslation() {
			return firstSentenceTranslation;
		}

		private String secondSentence() {
			return secondSentence;
		}

		private String secondSentenceTranslation() {
			return secondSentenceTranslation;
		}

		private SegmentBundleTokenizedEvent newSegmentEvent() {
			return new SegmentBundleTokenizedEvent(userId, tokenizedWord, tokenizedWordTranslation, firstSentence,
					firstSentenceTranslation, word, wordTranslation, sourceLanguage, targetLanguage, occurredAt);
		}

		private SegmentBundleTokenizedEvent newUsageForExistingSegmentEvent() {
			return new SegmentBundleTokenizedEvent(userId, tokenizedWord, tokenizedWordTranslation, secondSentence,
					secondSentenceTranslation, word, wordTranslation, sourceLanguage, targetLanguage, occurredAt);
		}

	}

}
