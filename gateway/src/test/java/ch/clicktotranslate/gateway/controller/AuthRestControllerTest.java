package ch.clicktotranslate.gateway.controller;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.TestingAuthenticationToken;
import ch.clicktotranslate.gateway.auth.CurrentUserQuery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthRestControllerTest {

	private final TestContext testContext = new TestContext();

	private final AuthRestController underTest = testContext.underTest();

	@Test
	void given_login_request_when_login_then_redirects_to_oauth_authorization_path() {
		var response = underTest.login();

		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		assertNotNull(response.getHeaders().getLocation());
		assertEquals("/oauth2/authorization/keycloak", response.getHeaders().getLocation().toString());
	}

	@Test
	void given_unauthenticated_user_when_me_then_returns_unauthorized() {
		when(testContext.currentUserQuery.resolveName(any())).thenReturn(Optional.empty());

		var response = underTest.me(null);

		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	void given_authenticated_user_when_me_then_returns_current_user_name() {
		TestingAuthenticationToken authentication = new TestingAuthenticationToken("dev-user", "n/a");
		when(testContext.currentUserQuery.resolveName(authentication)).thenReturn(Optional.of("Dev User"));

		var response = underTest.me(authentication);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Dev User", response.getBody().name());
		verify(testContext.currentUserQuery).resolveName(authentication);
	}

	private static final class TestContext {

		private final CurrentUserQuery currentUserQuery = mock(CurrentUserQuery.class);

		private AuthRestController underTest() {
			return new AuthRestController(currentUserQuery);
		}

	}

}
