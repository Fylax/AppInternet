package it.polito.ai.springserver.authorization.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

@PropertySource("classpath:authorization/security.properties")
public class ClientDetailsImpl implements ClientDetails {

  private OAuth2Client client;

  @Value("#{Set.of('${spring.granted_types}'.split(','))}")
  private Set<String> granted_types;

  @Value("${spring.accessTokenValiditySecond}")
  private int accessTokenValiditySeconds;

  @Value("${spring.refreshTokenValiditySecond}")
  private int refreshTokenValiditySeconds;

  @Autowired
  public ClientDetailsImpl(OAuth2Client client) {
    this.client = client;
  }

  @Override
  public String getClientId() {
    return client.getClientId();
  }

  @Override
  public Set<String> getResourceIds() {
    return new HashSet<>();
  }

  @Override
  public boolean isSecretRequired() {
    return true;
  }

  @Override
  public String getClientSecret() {
    return client.getClientSecret();
  }

  @Override
  public boolean isScoped() {
    return false;
  }

  @Override
  public Set<String> getScope() {
    return new HashSet<>();
  }

  @Override
  public Set<String> getAuthorizedGrantTypes() {
    return granted_types;
  }

  @Override
  public Set<String> getRegisteredRedirectUri() {
    return null;
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return new ArrayList<>();
  }

  @Override
  public Integer getAccessTokenValiditySeconds() {
    return accessTokenValiditySeconds;
  }

  @Override
  public Integer getRefreshTokenValiditySeconds() {
    return refreshTokenValiditySeconds;
  }

  @Override
  public boolean isAutoApprove(String scope) {
    return true;
  }

  @Override
  public Map<String, Object> getAdditionalInformation() {
    return null;
  }
}
