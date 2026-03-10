package ch.clicktotranslate.gateway.auth;

import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CurrentUserQueryTest {

	private final TestContext testContext = new TestContext();

	private final CurrentUserQuery underTest = testContext.underTest();

	@Test
	void given_unauthenticated_user_when_resolve_name_then_returns_empty() {
		Optional<String> name = underTest.resolveName(null);

		assertTrue(name.isEmpty());
	}

	@Test
	void given_oidc_user_with_name_claim_when_resolve_name_then_returns_name_claim() {
		TestingAuthenticationToken authentication = testContext.authenticatedWithOidcClaims(Map.of("name", "Dev User"));

		Optional<String> name = underTest.resolveName(authentication);

		assertEquals(Optional.of("Dev User"), name);
	}

	@Test
	void given_oauth2_user_with_preferred_username_when_resolve_name_then_returns_preferred_username() {
		TestingAuthenticationToken authentication = testContext
			.authenticatedWithOAuth2Attributes(Map.of("preferred_username", "dev-user"));

		Optional<String> name = underTest.resolveName(authentication);

		assertEquals(Optional.of("dev-user"), name);
	}

	private static final class TestContext {

		private CurrentUserQuery underTest() {
			return new CurrentUserQuery();
		}

		private TestingAuthenticationToken authenticatedWithOidcClaims(Map<String, Object> claims) {
			OidcUser oidcUser = mock(OidcUser.class);
			when(oidcUser.getClaims()).thenReturn(claims);
			TestingAuthenticationToken authentication = new TestingAuthenticationToken(oidcUser, "n/a");
			authentication.setAuthenticated(true);
			return authentication;
		}

		private TestingAuthenticationToken authenticatedWithOAuth2Attributes(Map<String, Object> attributes) {
			OAuth2User oauth2User = mock(OAuth2User.class);
			when(oauth2User.getAttributes()).thenReturn(attributes);
			TestingAuthenticationToken authentication = new TestingAuthenticationToken(oauth2User, "n/a");
			authentication.setAuthenticated(true);
			return authentication;
		}

	}

}
