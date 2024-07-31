package org.trainee.userservice.components;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KeycloakLogoutHandler implements LogoutHandler {

    private final RestTemplate restTemplate;

    public KeycloakLogoutHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String endSessionEndpoint = oidcUser.getIssuer() + "/protocol/openid-connect/logout";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endSessionEndpoint)
                .queryParam("id_token_hint", oidcUser.getIdToken().getTokenValue());
        restTemplate.getForEntity(builder.toUriString(), String.class);
    }
}
