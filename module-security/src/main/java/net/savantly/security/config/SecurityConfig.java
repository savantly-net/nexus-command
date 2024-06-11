package net.savantly.security.config;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.savantly.security.jwt.ProxyBearerTokenResolver;
import net.savantly.security.listener.LoginSuccessListener;
import net.savantly.security.oauth.OAuth2ClientService;
import net.savantly.security.oauth.OauthConfigProperties;
import net.savantly.security.preauthenticated.PreAuthConfigProperties;
import net.savantly.security.preauthenticated.PreAuthFilter;
import net.savantly.security.users.UserProvider;
import net.savantly.security.users.UserProviderImpl;

/**
 * This class represents a configuration class that is used to configure the
 * security of the application.
 */
@Slf4j
@EnableWebSecurity
@ConfigurationProperties(prefix = "app.security")
// @EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig implements ApplicationContextAware {

	@Setter
	private String bearerTokenHeaderName = "x-forwarded-access-token";

	@Setter
	private String authoritiesClaimName = "roles";

	@Setter
	private boolean debug = false;

	@Setter
	private boolean useCsrf = false;

	@Setter
	private boolean enabled = false;

	@Setter
	private String authorityPrefix = "ROLE_";

	//@Setter
	//private OauthConfigProperties oauth = new OauthConfigProperties();

	@Setter
	private PreAuthConfigProperties preauth = new PreAuthConfigProperties();

	private ApplicationContext applicationContext;

	@Bean
	@ConditionalOnBean(OAuth2AuthorizedClientService.class)
	public OAuth2ClientService oAuth2ClientService(OAuth2AuthorizedClientService authorizedClientService) {
		return new OAuth2ClientService(authorizedClientService);
	}

	@Bean
	public LoginSuccessListener loginSuccessListener() {
		return new LoginSuccessListener();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> {
			web.debug(debug);
			web.ignoring().requestMatchers("/");
			if (!enabled) {
				web.ignoring().requestMatchers("/**");
			}
		};
	}

	@Bean
	public SecurityFilterChain clientFilterChain(HttpSecurity http, AuthenticationManager authenticationManager)
			throws Exception {

		http.authenticationManager(authenticationManager);

		if (enabled) {
			log.info("security enabled. creating clientFilterChain");
			var permitAllMatchers = new AntPathRequestMatcher[] {
					new AntPathRequestMatcher("/"),
					new AntPathRequestMatcher("/login/**"),
					new AntPathRequestMatcher("/logged-out"),
					new AntPathRequestMatcher("/error/**"),
					new AntPathRequestMatcher("/webjars/**"),
					new AntPathRequestMatcher("/oauth**"),
					new AntPathRequestMatcher("/api/public/**"),
					new AntPathRequestMatcher("/actuator/health"),
			};

			log.info("permitAllMatchers: {}", permitAllMatchers.length);
			for (AntPathRequestMatcher antPathRequestMatcher : permitAllMatchers) {
				log.info("permitAll matcher: {}", antPathRequestMatcher.getPattern());
			}

			http.authorizeHttpRequests((authorize) -> authorize
					.requestMatchers(permitAllMatchers).permitAll()
					.anyRequest().authenticated());

			enableOauthIfEnabled(http);
			if (preauth.isEnabled()) {
				log.info("adding preauth filter");
				http.addFilter(new PreAuthFilter(preauth, authenticationManager));
			}
		} else {
			http.authorizeHttpRequests(authz -> authz.requestMatchers("/**").permitAll());
		}

		if (!useCsrf) {
			http.csrf(c -> c.disable());
		}

		log.info("", http);

		return http.build();
	}

	@Bean
	@ConditionalOnMissingBean
	public UserProvider userProvider() {
		return new UserProviderImpl();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return authentication -> {
			log.info("authentication: {}", authentication);
			return authentication;
		};
	}

	private void enableOauthIfEnabled(HttpSecurity http) throws Exception {

		var hasJwtDecoder = (applicationContext.getBeansOfType(JwtDecoder.class).size() > 0);
		if (hasJwtDecoder) {
			log.info("jwtDecoder bean found. Configuring OAuth2 resource server");
			http.oauth2ResourceServer(oauth2 -> oauth2.bearerTokenResolver(bearerTokenResolver())
					.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
			log.info("OAuth2 is enabled. Configuring OAuth2 client");
			http.oauth2Client(Customizer.withDefaults());
		} else {
			log.warn("OAuth2 is enabled but no JwtDecoder bean is available. OAuth2 will not be enabled.");
		}
		var hasOauth2Client = (applicationContext.getBeansOfType(OAuth2ClientService.class).size() > 0);
		if (hasOauth2Client) {
			log.info("OAuth2 is enabled. Configuring OAuth2 login");
			http.oauth2Login(Customizer.withDefaults());
		} else {
			log.warn("OAuth2 is enabled but no ClientRegistration bean is available. OAuth2 Login will not be enabled.");
		}
	}

	private AuthorizationGrantType getGrantType(String grantType) {
		var lower = grantType.toLowerCase();
		switch (lower) {
		case "authorization_code":
			return AuthorizationGrantType.AUTHORIZATION_CODE;
		case "client_credentials":
			return AuthorizationGrantType.CLIENT_CREDENTIALS;
		case "password":
			return AuthorizationGrantType.PASSWORD;
		case "refresh_token":
			return AuthorizationGrantType.REFRESH_TOKEN;
		default:
			return AuthorizationGrantType.AUTHORIZATION_CODE;
		}
	}

	private BearerTokenResolver bearerTokenResolver() {
		ProxyBearerTokenResolver bearerTokenResolver = new ProxyBearerTokenResolver();
		bearerTokenResolver.setBearerTokenHeaderName(bearerTokenHeaderName);
		// var bearerTokenResolver = new
		// HeaderBearerTokenResolver(bearerTokenHeaderName);
		return bearerTokenResolver;
	}

	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		// create a custom JWT converter to map the roles from the token as granted
		// authorities
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(authoritiesClaimName); // default is: scope, scp
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix(authorityPrefix); // default is: SCOPE_

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}