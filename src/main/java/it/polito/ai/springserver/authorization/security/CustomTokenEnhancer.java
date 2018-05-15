package it.polito.ai.springserver.authorization.security;

import it.polito.ai.springserver.authorization.model.User;
import it.polito.ai.springserver.authorization.model.UserDetailsImpl;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

      Map<String, Object> additionalInfo = new HashMap<>();
      UserDetailsImpl u = (UserDetailsImpl) authentication.getUserAuthentication().getPrincipal();
      additionalInfo.put("user_id", u.getUserId());
      ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
      return accessToken;
    }
}

