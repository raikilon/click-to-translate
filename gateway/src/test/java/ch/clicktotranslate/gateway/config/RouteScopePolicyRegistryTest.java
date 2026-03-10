package ch.clicktotranslate.gateway.config;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.server.mvc.config.GatewayMvcProperties;
import org.springframework.cloud.gateway.server.mvc.config.PredicateProperties;
import org.springframework.cloud.gateway.server.mvc.config.RouteProperties;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RouteScopePolicyRegistryTest {

	private final TestContext testContext = new TestContext();

	private RouteScopePolicyRegistry underTest;

	@Test
	void given_route_with_metadata_when_resolve_required_scope_then_scope_is_returned() {
		underTest = testContext.registryWithRoutes(testContext.route("segment-api", "/segment,/segment/**", "segment"));

		String resolvedScope = underTest.resolveRequiredScope(testContext.request("/segment")).orElseThrow();

		assertEquals("segment", resolvedScope);
	}

	@Test
	void given_route_without_required_scope_metadata_when_create_registry_then_throws_exception() {
		assertThrows(IllegalStateException.class,
				() -> testContext.registryWithRoutes(testContext.routeWithoutScope("segment-api", "/segment/**")));
	}

	@Test
	void given_route_with_metadata_when_matcher_checks_request_then_protected_route_matches() {
		underTest = testContext.registryWithRoutes(testContext.route("translate-api", "/translate/**", "translate"));

		boolean matches = underTest.protectedRoutesMatcher().matches(testContext.request("/translate/languages"));

		assertEquals(true, matches);
	}

	private static final class TestContext {

		private RouteScopePolicyRegistry registryWithRoutes(RouteProperties... routes) {
			GatewayMvcProperties properties = new GatewayMvcProperties();
			properties.setRoutes(List.of(routes));
			return new RouteScopePolicyRegistry(properties);
		}

		private RouteProperties route(String id, String pathPatterns, String scope) {
			RouteProperties route = routeWithoutScope(id, pathPatterns);
			route.setMetadata(Map.of("required-scope", scope));
			return route;
		}

		private RouteProperties routeWithoutScope(String id, String pathPatterns) {
			RouteProperties route = new RouteProperties();
			route.setId(id);
			route.setPredicates(List.of(pathPredicate(pathPatterns)));
			return route;
		}

		private PredicateProperties pathPredicate(String pathPatterns) {
			PredicateProperties predicate = new PredicateProperties();
			predicate.setName("Path");
			predicate.setArgs(Map.of("patterns", pathPatterns));
			return predicate;
		}

		private MockHttpServletRequest request(String path) {
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.setRequestURI(path);
			return request;
		}

	}

}
