package ch.clicktotranslate.gateway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ch.clicktotranslate.gateway.routing.ApiRoutes;

@Configuration
public class SecurityConfiguration {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, RouteScopePolicyRegistry routeScopePolicyRegistry,
			RouteScopeAuthorizationManager routeScopeAuthorizationManager) {
		var protectedRoutesMatcher = routeScopePolicyRegistry.protectedRoutesMatcher();
		return http.csrf(AbstractHttpConfigurer::disable)
			.cors(Customizer.withDefaults())
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(ApiRoutes.AUTH_LOGIN, ApiRoutes.AUTH_ME, ApiRoutes.AUTH_LOGOUT, ApiRoutes.OAUTH2_ALL,
						ApiRoutes.LOGIN_OAUTH2_ALL)
				.permitAll()
				.requestMatchers(protectedRoutesMatcher)
				.access(routeScopeAuthorizationManager)
				.anyRequest()
				.denyAll())
			.oauth2Login(Customizer.withDefaults())
			.logout(logout -> logout.logoutUrl(ApiRoutes.AUTH_LOGOUT)
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutSuccessHandler(
						(request, response, authentication) -> response.setStatus(HttpStatus.NO_CONTENT.value())))
			.exceptionHandling(exceptionHandling -> exceptionHandling.defaultAuthenticationEntryPointFor(
					new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), protectedRoutesMatcher))
			.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource(CorsProperties corsProperties) {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(corsProperties.allowedOrigins());
		configuration.setAllowCredentials(true);
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of(HttpHeaders.SET_COOKIE));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
