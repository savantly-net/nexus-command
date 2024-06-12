package net.savantly.security.users;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserProviderConfiguration {
    

	@Bean
	@ConditionalOnMissingBean
	public UserProvider userProvider() {
		return new UserProviderImpl();
	}
}
