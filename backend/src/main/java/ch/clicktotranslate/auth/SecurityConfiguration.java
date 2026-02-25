package ch.clicktotranslate.auth;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

	private static final String KEYCLOAK_CLIENT_ID = "click-to-translate-extension";

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		return http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/api/translate/languages", "/api/translate/languages/**", "/api/translate/text",
						"/api/translate/text/**")
				.access(hasScope("translate"))
				.requestMatchers("/api/translate", "/api/translate/")
				.access(hasScope("segment"))
				.requestMatchers("/api/vocabulary/**")
				.access(hasScope("vocabulary"))
				.requestMatchers("/actuator/health", "/actuator/health/**", "/actuator/info")
				.permitAll()
				.anyRequest()
				.denyAll())
			.oauth2ResourceServer(
					oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.build();
	}

	@Bean
	Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
		var scopeAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		var keycloakRolesConverter = new KeycloakRolesConverter(KEYCLOAK_CLIENT_ID);

		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(jwt -> Stream
			.concat(scopeAuthoritiesConverter.convert(jwt).stream(), keycloakRolesConverter.convert(jwt).stream())
			.collect(Collectors.toUnmodifiableSet()));
		return converter;
	}

}
