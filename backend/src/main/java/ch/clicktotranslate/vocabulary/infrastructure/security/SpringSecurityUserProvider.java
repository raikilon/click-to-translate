package ch.clicktotranslate.vocabulary.infrastructure.security;

import ch.clicktotranslate.vocabulary.application.UserProvider;
import ch.clicktotranslate.vocabulary.domain.UserId;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SpringSecurityUserProvider implements UserProvider {

	@Override
	public UserId currentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new IllegalStateException("Authenticated user is required");
		}
		String name = authentication.getName();
		if (isUsable(name)) {
			return UserId.of(name);
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof Jwt jwt && isUsable(jwt.getSubject())) {
			return UserId.of(jwt.getSubject());
		}
		if (principal instanceof Map<?, ?> map) {
			Object sub = map.get("sub");
			if (sub instanceof String subject && isUsable(subject)) {
				return UserId.of(subject);
			}
		}
		throw new IllegalStateException("Authenticated user identifier is missing");
	}

	private boolean isUsable(String value) {
		return value != null && !value.isBlank() && !"anonymousUser".equalsIgnoreCase(value);
	}

}
