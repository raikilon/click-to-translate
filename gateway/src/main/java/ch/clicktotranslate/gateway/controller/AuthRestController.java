package ch.clicktotranslate.gateway.controller;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ch.clicktotranslate.gateway.auth.CurrentUserQuery;
import ch.clicktotranslate.gateway.routing.ApiRoutes;

@RestController
@RequestMapping(ApiRoutes.AUTH_BASE)
public class AuthRestController {

	private final CurrentUserQuery currentUserQuery;

	public AuthRestController(CurrentUserQuery currentUserQuery) {
		this.currentUserQuery = currentUserQuery;
	}

	@GetMapping("/login")
	public ResponseEntity<Void> login() {
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/oauth2/authorization/keycloak")).build();
	}

	@GetMapping("/me")
	public ResponseEntity<CurrentUserResponse> me(Authentication authentication) {
		return currentUserQuery.resolveUsername(authentication)
			.map(username -> ResponseEntity.ok(new CurrentUserResponse(username)))
			.orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}

}
