package ch.clicktotranslate.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
		throw new IllegalStateException("Authenticated user identifier is missing");
	}

	private boolean isUsable(String value) {
		return value != null && !value.isBlank();
	}

}
