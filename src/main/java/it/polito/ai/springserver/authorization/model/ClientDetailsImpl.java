package it.polito.ai.springserver.authorization.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

public class ClientDetailsImpl implements ClientDetails {

  private OAuth2Client client;
  private ClientConfiguration configuration;

  @Autowired
  public ClientDetailsImpl(OAuth2Client client, ClientConfiguration configuration) {
    this.client = client;
    this.configuration = configuration;
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
    return false;
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
    return Set.of(new String[] {"read", "write"});
  }

  @Override
  public Set<String> getAuthorizedGrantTypes() {
    return configuration.getGrantedTypes();
  }

  @Override
  public Set<String> getRegisteredRedirectUri() {
    return new HashSet<>();
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return new ArrayList<>();
  }

  @Override
  public Integer getAccessTokenValiditySeconds() {
    return configuration.getAccessTokenValiditySeconds();
  }

  @Override
  public Integer getRefreshTokenValiditySeconds() {
    return configuration.getRefreshTokenValiditySeconds();
  }

  @Override
  public boolean isAutoApprove(String scope) {
    return true;
  }

  @Override
  public Map<String, Object> getAdditionalInformation() {
    return new HashMap<>();
  }
}
