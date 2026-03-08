package ch.clicktotranslate.gateway.config;

import java.util.*;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.gateway.server.mvc.config.GatewayMvcProperties;
import org.springframework.cloud.gateway.server.mvc.config.PredicateProperties;
import org.springframework.cloud.gateway.server.mvc.config.RouteProperties;
import org.springframework.http.server.PathContainer;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

@Component
public class RouteScopePolicyRegistry {

	private static final String PATH_PREDICATE_NAME = "Path";

	private static final String REQUIRED_SCOPE_METADATA_KEY = "required-scope";

	private final List<RouteScopePolicy> policies;

	private final RequestMatcher protectedRoutesMatcher;

	public RouteScopePolicyRegistry(GatewayMvcProperties gatewayMvcProperties) {
		this.policies = resolvePolicies(gatewayMvcProperties);
		this.protectedRoutesMatcher = request -> resolveRequiredScope(request).isPresent();
	}

	public Optional<String> resolveRequiredScope(HttpServletRequest request) {
		String path = extractRequestPath(request);
		PathContainer parsedPath = PathContainer.parsePath(path);
		for (RouteScopePolicy policy : policies) {
			if (policy.matches(parsedPath)) {
				return Optional.of(policy.requiredScope());
			}
		}
		return Optional.empty();
	}

	public RequestMatcher protectedRoutesMatcher() {
		return protectedRoutesMatcher;
	}

	private List<RouteScopePolicy> resolvePolicies(GatewayMvcProperties gatewayMvcProperties) {
		List<RouteProperties> routes = collectRoutes(gatewayMvcProperties);
		PathPatternParser parser = new PathPatternParser();
		List<RouteScopePolicy> resolved = new ArrayList<>();
		for (RouteProperties route : routes) {
			RouteScopePolicy policy = toPolicy(route, parser);
			if (policy != null) {
				resolved.add(policy);
			}
		}

		if (resolved.isEmpty()) {
			throw new IllegalStateException("No gateway route scope policy has been configured");
		}

		return List.copyOf(resolved);
	}

	private List<RouteProperties> collectRoutes(GatewayMvcProperties gatewayMvcProperties) {
		List<RouteProperties> routes = new ArrayList<>();
		List<RouteProperties> directRoutes = gatewayMvcProperties.getRoutes();
		for (RouteProperties route : directRoutes) {
			if (route != null) {
				routes.add(route);
			}
		}

		Map<String, RouteProperties> routeMap = gatewayMvcProperties.getRoutesMap();
		for (RouteProperties route : routeMap.values()) {
			if (route != null) {
				routes.add(route);
			}
		}

		return routes;
	}

	private RouteScopePolicy toPolicy(RouteProperties route, PathPatternParser parser) {
		List<String> pathPatterns = resolvePathPatterns(route);
		if (pathPatterns.isEmpty()) {
			return null;
		}

		String requiredScope = resolveRequiredScope(route);
		List<PathPattern> compiledPatterns = pathPatterns.stream().map(parser::parse).toList();
		return new RouteScopePolicy(requiredScope, compiledPatterns);
	}

	private List<String> resolvePathPatterns(RouteProperties route) {
		List<PredicateProperties> predicates = route.getPredicates();

		List<String> patterns = new ArrayList<>();
		for (PredicateProperties predicate : predicates) {
			if (predicate == null || !PATH_PREDICATE_NAME.equalsIgnoreCase(predicate.getName())) {
				continue;
			}
			patterns.addAll(extractPathArguments(predicate));
		}

		return patterns.stream().map(String::trim).filter(pattern -> !pattern.isBlank()).toList();
	}

	private List<String> extractPathArguments(PredicateProperties predicate) {
		Map<String, String> args = predicate.getArgs();

		List<String> result = new ArrayList<>();
		for (String raw : args.values()) {
			if (raw == null) {
				continue;
			}
			result.addAll(Arrays.asList(raw.split(",")));
		}
		return result;
	}

	private String resolveRequiredScope(RouteProperties route) {
		Map<String, Object> metadata = route.getMetadata();
		Object requiredScope = metadata.get(REQUIRED_SCOPE_METADATA_KEY);
		if (!(requiredScope instanceof String value) || value.isBlank()) {
			throw new IllegalStateException("Route '" + route.getId() + "' must define metadata.required-scope");
		}
		return value;
	}

	private String extractRequestPath(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (contextPath == null || contextPath.isBlank()) {
			return uri;
		}
		if (uri.startsWith(contextPath)) {
			return uri.substring(contextPath.length());
		}
		return uri;
	}

	private record RouteScopePolicy(String requiredScope, List<PathPattern> patterns) {

		private boolean matches(PathContainer parsedPath) {
			return patterns.stream().anyMatch(pattern -> pattern.matches(parsedPath));
		}

	}

}
