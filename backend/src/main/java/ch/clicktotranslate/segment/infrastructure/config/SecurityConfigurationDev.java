package ch.clicktotranslate.segment.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")
public class SecurityConfigurationDev {

	@Bean
	public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.build();
	}

}
