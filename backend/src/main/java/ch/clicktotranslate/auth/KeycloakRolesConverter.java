package ch.clicktotranslate.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

final class KeycloakRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	private final String clientId;

	KeycloakRolesConverter(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		Set<String> roles = new HashSet<>();

		Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
		if (resourceAccess != null) {
			Object client = resourceAccess.get(this.clientId);
			if (client instanceof Map<?, ?> map) {
				Object clientRoles = map.get("roles");
				if (clientRoles instanceof Collection<?> collection) {
					collection.forEach(role -> roles.add(String.valueOf(role)));
				}
			}
		}

		return roles.stream()
			.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
			.collect(Collectors.toUnmodifiableSet());
	}

}
