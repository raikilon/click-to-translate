package ch.clicktotranslate.segment;

import ch.clicktotranslate.auth.UserId;
import ch.clicktotranslate.auth.UserProvider;
import ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent;
import ch.clicktotranslate.segment.infrastructure.LanguageDto;
import ch.clicktotranslate.segment.infrastructure.SegmentBundleDto;
import ch.clicktotranslate.segment.infrastructure.SegmentBundleTranslationRestController;
import ch.clicktotranslate.segment.infrastructure.TranslatedSegmentDto;
import ch.clicktotranslate.translation.infrastructure.TextToTranslateDto;
import ch.clicktotranslate.translation.infrastructure.TextTranslationFacade;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.JpaEntryEntity;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.JpaUsageEntity;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataEntryRepository;
import ch.clicktotranslate.vocabulary.infrastructure.persistence.SpringDataUsageRepository;
import java.time.Duration;
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
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.ALL_DEPENDENCIES, module = "segment",
		extraIncludes = { "lemmatizer", "vocabulary" })
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:segment-to-vocabulary-integration-test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
		"spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa",
		"spring.datasource.password=", "spring.jpa.hibernate.ddl-auto=create-drop" })
class SegmentToVocabularyIntegrationTest {

	@Autowired
	private SegmentBundleTranslationRestController underTest;

	@Autowired
	private SpringDataEntryRepository entryRepository;

	@Autowired
	private SpringDataUsageRepository usageRepository;

	@MockitoBean
	private TextTranslationFacade textTranslationFacade;

	@MockitoBean
	private UserProvider userProvider;

	@MockitoBean
	private JwtDecoder jwtDecoder;

	@BeforeEach
	void setUp() {
		usageRepository.deleteAll();
		entryRepository.deleteAll();
		given(userProvider.currentUserId()).willReturn(UserId.of("user-1"));
	}

	@Test
	void givenSegmentRequest_whenChainSucceeds_thenCreatesVocabularyEntryFromLemmatizedEvent(Scenario scenario) {
		TestContext context = new TestContext();
		context.givenSuccessfulTranslations();

		scenario.stimulate(() -> underTest.translate(context.segmentBundle()))
			.andWaitAtMost(Duration.ofSeconds(3))
			.andWaitForStateChange(context::entriesByUser, entries -> entries.size() == 1)
			.andVerify((entries, response) -> {
				assertThat(response).isEqualTo(context.expectedSegmentResponse());
				assertThat(entries).hasSize(1);
				JpaEntryEntity entry = entries.getFirst();
				assertThat(entry.getUserId()).isEqualTo(context.userId());
				assertThat(entry.getLanguage()).isEqualTo(context.sourceLanguage());
				assertThat(entry.getTerm()).isEqualTo(context.expectedStoredTerm());

				List<JpaUsageEntity> usages = context.usagesByEntry(entry.getId());
				assertThat(usages).hasSize(1);
				JpaUsageEntity usage = usages.getFirst();
				assertThat(usage.getSentence()).isEqualTo(context.sentence());
				assertThat(usage.getTranslation()).isEqualTo(context.sentenceTranslation());
				assertThat(usage.getLanguage()).isEqualTo(context.targetLanguage());
			});

		context.verifyAllTranslationCalls();
	}

	@Test
	void givenSegmentRequest_whenLemmatizerFails_thenSegmentResponseStillSucceedsAndNoVocabularyEntryIsWritten(
			Scenario scenario) {
		TestContext context = new TestContext();
		context.givenSegmentTranslationsButFailLemmatizer();

		scenario.stimulate(() -> underTest.translate(context.segmentBundle()))
			.andWaitAtMost(Duration.ofSeconds(3))
			.andWaitForEventOfType(SegmentBundleCreatedEvent.class)
			.matching(context::segmentCreatedEventMatches)
			.toArriveAndVerify((event, response) -> assertThat(response).isEqualTo(context.expectedSegmentResponse()));

		verify(textTranslationFacade, timeout(3000).times(1)).translate(context.lemmatizerWordTranslationRequest());
		assertThat(context.entriesByUser()).isEmpty();

		context.verifySegmentTranslationCalls();
	}

	private final class TestContext {

		private final Instant occurredAt = Instant.parse("2026-02-07T10:15:30Z");

		private final String userId = "user-1";

		private final String word = "Haus Katze";

		private final String lemmatizedWord = "Haus";

		private final String sentence = "Die Haus Katze ist gross.";

		private final String segmentWordTranslation = "House cat";

		private final String sentenceTranslation = "The House cat is big.";

		private final String lemmatizedWordTranslation = "House";

		private final String sourceLanguage = "DE";

		private final String targetLanguage = "EN";

		private final SegmentBundleDto segmentBundle = new SegmentBundleDto(word, sentence, LanguageDto.DE,
				LanguageDto.EN, occurredAt);

		private final TextToTranslateDto segmentWordTranslationRequest = new TextToTranslateDto(word,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.DE,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.EN, sentence);

		private final TextToTranslateDto segmentSentenceTranslationRequest = new TextToTranslateDto(sentence,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.DE,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.EN, null);

		private final TextToTranslateDto lemmatizerWordTranslationRequest = new TextToTranslateDto(lemmatizedWord,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.DE,
				ch.clicktotranslate.translation.infrastructure.LanguageDto.EN, sentence);

		private SegmentBundleDto segmentBundle() {
			return segmentBundle;
		}

		private TranslatedSegmentDto expectedSegmentResponse() {
			return new TranslatedSegmentDto(segmentWordTranslation);
		}

		private String userId() {
			return userId;
		}

		private String sourceLanguage() {
			return sourceLanguage;
		}

		private String targetLanguage() {
			return targetLanguage;
		}

		private String expectedStoredTerm() {
			return lemmatizedWord.toLowerCase();
		}

		private String sentence() {
			return sentence;
		}

		private String sentenceTranslation() {
			return sentenceTranslation;
		}

		private TextToTranslateDto lemmatizerWordTranslationRequest() {
			return lemmatizerWordTranslationRequest;
		}

		private boolean segmentCreatedEventMatches(SegmentBundleCreatedEvent event) {
			return event.userId().equals(userId) && event.word().equals(word) && event.sentence().equals(sentence)
					&& event.wordTranslation().equals(segmentWordTranslation)
					&& event.sentenceTranslation().equals(sentenceTranslation)
					&& event.sourceLanguage().equals(sourceLanguage) && event.targetLanguage().equals(targetLanguage)
					&& event.occurredAt().equals(occurredAt);
		}

		private List<JpaEntryEntity> entriesByUser() {
			return entryRepository.findByUserId(userId, PageRequest.of(0, 100)).getContent();
		}

		private List<JpaUsageEntity> usagesByEntry(Long entryId) {
			return usageRepository.findByEntryIdAndEntryUserId(entryId, userId, PageRequest.of(0, 100)).getContent();
		}

		private void givenSuccessfulTranslations() {
			given(textTranslationFacade.translate(segmentWordTranslationRequest)).willReturn(segmentWordTranslation);
			given(textTranslationFacade.translate(segmentSentenceTranslationRequest)).willReturn(sentenceTranslation);
			given(textTranslationFacade.translate(lemmatizerWordTranslationRequest))
				.willReturn(lemmatizedWordTranslation);
		}

		private void givenSegmentTranslationsButFailLemmatizer() {
			given(textTranslationFacade.translate(segmentWordTranslationRequest)).willReturn(segmentWordTranslation);
			given(textTranslationFacade.translate(segmentSentenceTranslationRequest)).willReturn(sentenceTranslation);
			given(textTranslationFacade.translate(lemmatizerWordTranslationRequest))
				.willThrow(new IllegalStateException("lemmatizer failed"));
		}

		private void verifyAllTranslationCalls() {
			verify(textTranslationFacade, times(1)).translate(segmentWordTranslationRequest);
			verify(textTranslationFacade, times(1)).translate(segmentSentenceTranslationRequest);
			verify(textTranslationFacade, timeout(3000).times(1)).translate(lemmatizerWordTranslationRequest);
		}

		private void verifySegmentTranslationCalls() {
			verify(textTranslationFacade, times(1)).translate(segmentWordTranslationRequest);
			verify(textTranslationFacade, times(1)).translate(segmentSentenceTranslationRequest);
		}

	}

}
