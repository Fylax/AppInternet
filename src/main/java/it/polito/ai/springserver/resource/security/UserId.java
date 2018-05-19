package it.polito.ai.springserver.resource.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserId {

  @Autowired
  private TokenStore tokenStore;

  public long getUserId() {
    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
    Map<String, Object> claims = accessToken.getAdditionalInformation();
    return Long.valueOf((String) claims.get("user_id"));
  }

}
