package ch.clicktotranslate.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SpringSecurityUserProvider implements UserProvider {

	public UserId currentUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			throw new IllegalStateException("Authenticated user is required");
		}

		String userId = auth.getName();
		if (userId == null || userId.isBlank() || "anonymousUser".equals(userId)) {
			throw new IllegalStateException("Authenticated user identifier is missing");
		}

		return UserId.of(userId);
	}

}
