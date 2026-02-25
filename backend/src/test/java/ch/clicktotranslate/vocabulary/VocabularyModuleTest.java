package ch.clicktotranslate.vocabulary;

import ch.clicktotranslate.auth.UserProvider;
import ch.clicktotranslate.auth.UserId;
import ch.clicktotranslate.lemmatizer.domain.SegmentBundleLemmatizedEvent;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.JpaEntryEntity;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.JpaUsageEntity;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataEntryRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataUsageRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ApplicationModuleTest
@TestPropertySource(
		properties = { "spring.datasource.url=jdbc:h2:mem:vocabulary-module-test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
				"spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa",
				"spring.datasource.password=", "spring.jpa.hibernate.ddl-auto=create-drop" })
class VocabularyModuleTest {

	@Autowired
	private SpringDataEntryRepository entryRepository;

	@Autowired
	private SpringDataUsageRepository usageRepository;

	@MockitoBean
	private JwtDecoder jwtDecoder;

	@MockitoBean
	private UserProvider userProvider;

	@BeforeEach
	void cleanDatabase() {
		entryRepository.deleteAll();
		given(userProvider.currentUserId()).willReturn(UserId.of("user-1"));
	}

	@Test
	void givenNewSegmentEvent_whenHandled_thenCreatesEntryWithUsage(Scenario scenario) {
		TestContext context = new TestContext();

		scenario.publish(context.newSegmentEvent())
			.andWaitForStateChange(context::entriesByUser, entries -> entries.size() == 1)
			.andVerify(entries -> {
				assertThat(entries).hasSize(1);
				JpaEntryEntity entry = entries.getFirst();
				assertThat(entry.getId()).isNotNull();
				assertThat(entry.getUserId()).isEqualTo(context.userId());
				assertThat(entry.getLanguage()).isEqualTo(context.sourceLanguage());
				assertThat(entry.getTerm()).isEqualTo(context.normalizedTerm());
				assertThat(entry.getTranslations())
					.extracting(translation -> translation.getLanguage() + ":" + translation.getTerm())
					.containsExactlyInAnyOrder(context.targetLanguage() + ":" + context.normalizedTermTranslation());

				List<JpaUsageEntity> usages = context.usages();
				assertThat(usages).hasSize(1);

				JpaUsageEntity usage = usages.getFirst();
				assertThat(usage.getSentence()).isEqualTo(context.firstSentence());
				assertThat(usage.getSentenceStart()).isEqualTo(4);
				assertThat(usage.getSentenceEnd()).isEqualTo(8);
				assertThat(usage.getTranslation()).isEqualTo(context.firstSentenceTranslation());
				assertThat(usage.getTranslationStart()).isEqualTo(4);
				assertThat(usage.getTranslationEnd()).isEqualTo(9);
				assertThat(usage.getLanguage()).isEqualTo(context.targetLanguage());
			});
	}

	@Test
	void givenExistingSegmentWithNewUsage_whenHandled_thenAddsOnlyNewUsage(Scenario scenario) {
		TestContext context = new TestContext();

		scenario.publish(context.newSegmentEvent())
			.andWaitForStateChange(context::usageCount, count -> count == 1)
			.andVerify(count -> assertThat(count).isEqualTo(1));

		scenario.publish(context.newUsageForExistingSegmentEvent())
			.andWaitForStateChange(context::usageCount, count -> count == 2)
			.andVerify(count -> {
				assertThat(count).isEqualTo(2);
				List<JpaUsageEntity> usages = context.usages();
				assertThat(usages).hasSize(2);
				assertThat(usages.stream().map(JpaUsageEntity::getSentence).toList())
					.containsExactlyInAnyOrder(context.firstSentence(), context.secondSentence());
				assertThat(usages.stream().map(JpaUsageEntity::getTranslation).toList())
					.containsExactlyInAnyOrder(context.firstSentenceTranslation(), context.secondSentenceTranslation());
			});
	}

	@Test
	void givenExistingSegmentAndExistingUsage_whenHandled_thenDoesNotCreateDuplicateUsage(Scenario scenario) {
		TestContext context = new TestContext();

		scenario.publish(context.newSegmentEvent())
			.andWaitForStateChange(context::usageCount, count -> count == 1)
			.andVerify(count -> assertThat(count).isEqualTo(1));

		scenario.publish(context.newSegmentEvent())
			.andWaitForStateChange(context::usageCount, count -> count == 1)
			.andVerify(count -> {
				assertThat(count).isEqualTo(1);
				assertThat(context.entriesByUser()).hasSize(1);
			});
	}

	@Test
	void givenTwentyStarredUsages_whenNewUsageEventHandled_thenSkipsAddingUsage(Scenario scenario) {
		TestContext context = new TestContext();
		context.seedEntryWithStarredUsages(20);

		scenario.publish(context.newUsageForExistingSegmentEvent())
			.andWaitForStateChange(context::usageCount, count -> count == 20)
			.andVerify(count -> {
				assertThat(count).isEqualTo(20);
				assertThat(context.usages()).allMatch(JpaUsageEntity::isStarred);
				assertThat(context.usages().stream().map(JpaUsageEntity::getSentence).toList())
					.doesNotContain(context.secondSentence());
			});
	}

	private final class TestContext {

		private final Instant occurredAt = Instant.parse("2026-02-07T10:15:30Z");

		private final String userId = "user-1";

		private final String term = "Haus";

		private final String termTranslation = "house";

		private final String secondTermTranslation = "maison";

		private final String sourceLanguage = "DE";

		private final String targetLanguage = "EN";

		private final String secondTargetLanguage = "FR";

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

		private String normalizedTerm() {
			return term.toLowerCase();
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

		private String normalizedTermTranslation() {
			return termTranslation.toLowerCase();
		}

		private String normalizedSecondTermTranslation() {
			return secondTermTranslation.toLowerCase();
		}

		private String secondTargetLanguage() {
			return secondTargetLanguage;
		}

		private SegmentBundleLemmatizedEvent newSegmentEvent() {
			return new SegmentBundleLemmatizedEvent(userId, term, termTranslation, firstSentence,
					firstSentenceTranslation, word, wordTranslation, sourceLanguage, targetLanguage, occurredAt);
		}

		private SegmentBundleLemmatizedEvent newUsageForExistingSegmentEvent() {
			return new SegmentBundleLemmatizedEvent(userId, term, termTranslation, secondSentence,
					secondSentenceTranslation, word, wordTranslation, sourceLanguage, targetLanguage, occurredAt);
		}

		private SegmentBundleLemmatizedEvent newUsageWithDifferentTargetLanguageEvent() {
			return new SegmentBundleLemmatizedEvent(userId, term, secondTermTranslation, secondSentence,
					secondSentenceTranslation, word, wordTranslation, sourceLanguage, secondTargetLanguage, occurredAt);
		}

		private List<JpaEntryEntity> entriesByUser() {
			return entryRepository.findByUserId(userId, PageRequest.of(0, 1_000)).getContent();
		}

		private int usageCount() {
			return usages().size();
		}

		private JpaEntryEntity findEntry() {
			return entryRepository.findByUserIdAndLanguageAndTerm(userId, sourceLanguage, normalizedTerm())
				.orElseThrow();
		}

		private List<JpaUsageEntity> usages() {
			return usageRepository.findByEntryIdAndEntryUserId(findEntry().getId(), userId, PageRequest.of(0, 1_000))
				.getContent();
		}

		private void seedEntryWithStarredUsages(int count) {
			JpaEntryEntity entry = new JpaEntryEntity();
			entry.setUserId(userId);
			entry.setLanguage(sourceLanguage);
			entry.setTerm(normalizedTerm());

			for (int i = 1; i <= count; i++) {
				JpaUsageEntity usage = new JpaUsageEntity();
				usage.setSentence("Starred usage sentence " + i);
				usage.setSentenceStart(0);
				usage.setSentenceEnd(4);
				usage.setTranslation("Starred usage translation " + i);
				usage.setTranslationStart(0);
				usage.setTranslationEnd(4);
				usage.setLanguage(targetLanguage);
				usage.setStarred(true);
				entry.addUsage(usage);
			}

			entryRepository.save(entry);
		}

	}

}
