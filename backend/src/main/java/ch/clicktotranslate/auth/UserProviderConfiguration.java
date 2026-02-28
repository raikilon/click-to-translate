package ch.clicktotranslate.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserProviderConfiguration {

	@Bean
	public UserProvider userProvider() {
		return new SpringSecurityUserProvider();
	}

}
