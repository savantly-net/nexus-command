package net.savantly.security.oauth;

import org.apache.causeway.extensions.secman.delegated.springoauth2.CausewayModuleExtSecmanDelegatedSpringOauth2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import lombok.RequiredArgsConstructor;

@Configuration
@ConditionalOnProperty(prefix = "app.security.oauth2.login", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Import(CausewayModuleExtSecmanDelegatedSpringOauth2.class)
public class OAuth2LoginConfig {

    private final OauthConfigProperties oauthConfigProperties;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository(
            OAuth2AuthorizedClientService authorizedClientService) {
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
    }

    private ClientRegistration googleClientRegistration() {
        var loginConfig = oauthConfigProperties.getLogin();
        return ClientRegistration.withRegistrationId(loginConfig.getRegistrationId())
                .clientId(loginConfig.getClientId())
                .clientSecret(loginConfig.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope(loginConfig.getScope().split(" "))
                .userNameAttributeName(loginConfig.getUsernameAttribute())
                .clientName(loginConfig.getName())
                .issuerUri(loginConfig.getIssuerUri())
                .authorizationUri(loginConfig.getAuthorizationUri())
                .tokenUri(loginConfig.getTokenUri())
                .userInfoUri(loginConfig.getUserInfoUri())
                .jwkSetUri(loginConfig.getJwkSetUri())
                .build();
    }
}