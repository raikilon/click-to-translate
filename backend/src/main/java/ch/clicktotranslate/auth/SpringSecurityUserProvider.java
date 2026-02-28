package ch.clicktotranslate.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

public class SpringSecurityUserProvider implements UserProvider {

	public UserId currentUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			throw new AuthenticationCredentialsNotFoundException("Authenticated user is required");
		}

		String userId = auth.getName();
		if (userId == null || userId.isBlank() || "anonymousUser".equals(userId)) {
			throw new AuthenticationCredentialsNotFoundException("Authenticated user identifier is missing");
		}

		return UserId.of(userId);
	}

}
