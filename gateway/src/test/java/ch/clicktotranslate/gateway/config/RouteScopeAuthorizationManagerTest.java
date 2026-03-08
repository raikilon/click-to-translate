package ch.clicktotranslate.gateway.config;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.server.mvc.config.GatewayMvcProperties;
import org.springframework.cloud.gateway.server.mvc.config.PredicateProperties;
import org.springframework.cloud.gateway.server.mvc.config.RouteProperties;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteScopeAuthorizationManagerTest {

	private final TestContext testContext = new TestContext();

	private RouteScopeAuthorizationManager underTest;

	@Test
	void given_authenticated_user_with_scope_when_authorize_then_access_is_granted() {
		underTest = testContext.managerWithRoute("translate-api", "/translate/**", "translate");

		boolean granted = Objects.requireNonNull(underTest
                        .authorize(() -> testContext.authenticatedUserWithScope(),
                                testContext.authorizationContext("/translate/languages")))
			.isGranted();

		assertTrue(granted);
	}

	@Test
	void given_authenticated_user_without_scope_when_authorize_then_access_is_denied() {
		underTest = testContext.managerWithRoute("vocabulary-api", "/vocabulary/**", "vocabulary");

		boolean granted = Objects.requireNonNull(underTest
                        .authorize(() -> testContext.authenticatedUserWithScope(),
                                testContext.authorizationContext("/vocabulary/entries")))
			.isGranted();

		assertFalse(granted);
	}

	private static final class TestContext {

		private RouteScopeAuthorizationManager managerWithRoute(String routeId, String pathPatterns,
				String requiredScope) {
			RouteScopePolicyRegistry registry = new RouteScopePolicyRegistry(
					gatewayProperties(routeId, pathPatterns, requiredScope));
			return new RouteScopeAuthorizationManager(registry);
		}

		private GatewayMvcProperties gatewayProperties(String routeId, String pathPatterns, String requiredScope) {
			PredicateProperties pathPredicate = new PredicateProperties();
			pathPredicate.setName("Path");
			pathPredicate.setArgs(Map.of("patterns", pathPatterns));

			RouteProperties route = new RouteProperties();
			route.setId(routeId);
			route.setPredicates(List.of(pathPredicate));
			route.setMetadata(Map.of("required-scope", requiredScope));

			GatewayMvcProperties properties = new GatewayMvcProperties();
			properties.setRoutes(List.of(route));
			return properties;
		}

		private TestingAuthenticationToken authenticatedUserWithScope() {
			TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "n/a", "SCOPE_translate");
			authentication.setAuthenticated(true);
			return authentication;
		}

		private RequestAuthorizationContext authorizationContext(String path) {
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.setRequestURI(path);
			return new RequestAuthorizationContext(request);
		}

	}

}
