package ch.clicktotranslate.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		return http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/api/translate/languages", "/api/translate/languages/**", "/api/translate/text",
						"/api/translate/text/**")
				.hasAuthority("SCOPE_translate")
				.requestMatchers("/api/translate", "/api/translate/")
				.hasAuthority("SCOPE_segment")
				.requestMatchers("/api/vocabulary/**")
				.hasAuthority("SCOPE_vocabulary")
				.requestMatchers("/actuator/health", "/actuator/health/**", "/actuator/info")
				.permitAll()
				.anyRequest()
				.denyAll())
			.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.build();
	}

}
