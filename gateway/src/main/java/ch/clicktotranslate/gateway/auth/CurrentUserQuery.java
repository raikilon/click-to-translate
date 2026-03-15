package ch.clicktotranslate.gateway.auth;

import java.util.Map;
import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserQuery {

	public Optional<String> resolveUsername(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication instanceof AnonymousAuthenticationToken) {
			return Optional.empty();
		}

		if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
			return resolveUsernameFromAttributes(oidcUser.getClaims()).or(() -> Optional.of(authentication.getName()));
		}

		if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
			return resolveUsernameFromAttributes(oauth2User.getAttributes())
				.or(() -> Optional.of(authentication.getName()));
		}

		return Optional.of(authentication.getName());
	}

	private Optional<String> resolveUsernameFromAttributes(Map<String, Object> attributes) {
		if (attributes == null) {
			return Optional.empty();
		}

		Object preferredUsername = attributes.get("preferred_username");
		if (preferredUsername instanceof String value && !value.isBlank()) {
			return Optional.of(value);
		}

		Object name = attributes.get("name");
		if (name instanceof String value && !value.isBlank()) {
			return Optional.of(value);
		}

		return Optional.empty();
	}

}
