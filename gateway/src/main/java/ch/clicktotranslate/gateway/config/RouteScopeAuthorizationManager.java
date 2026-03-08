package ch.clicktotranslate.gateway.config;

import java.util.Objects;
import java.util.function.Supplier;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

@Component
public class RouteScopeAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

	private final RouteScopePolicyRegistry routeScopePolicyRegistry;

	public RouteScopeAuthorizationManager(RouteScopePolicyRegistry routeScopePolicyRegistry) {
		this.routeScopePolicyRegistry = routeScopePolicyRegistry;
	}

	@Override
	public AuthorizationResult authorize(@NonNull Supplier<? extends Authentication> authentication,
			RequestAuthorizationContext context) {
		return routeScopePolicyRegistry.resolveRequiredScope(Objects.requireNonNull(context).getRequest())
			.map(requiredScope -> hasRequiredScope(authentication.get(), requiredScope))
			.map(AuthorizationDecision::new)
			.orElseGet(() -> new AuthorizationDecision(false));
	}

	private boolean hasRequiredScope(Authentication authentication, String requiredScope) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return false;
		}

		String requiredAuthority = "SCOPE_" + requiredScope;
		return authentication.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.anyMatch(requiredAuthority::equals);
	}

}
