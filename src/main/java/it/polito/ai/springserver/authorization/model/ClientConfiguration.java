package it.polito.ai.springserver.authorization.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Set;

@Configuration
@PropertySource("classpath:authorization/security.properties")
public class ClientConfiguration {

  @Value("#{'${spring.granted_types}'.split(',')}")
  private String[] granted_types;

  @Value("${spring.accessTokenValiditySecond}")
  private int accessTokenValiditySeconds;

  @Value("${spring.refreshTokenValiditySecond}")
  private int refreshTokenValiditySeconds;

  public Set<String> getGrantedTypes() {
    return Set.of(granted_types);
  }

  public int getAccessTokenValiditySeconds() {
    return accessTokenValiditySeconds;
  }

  public int getRefreshTokenValiditySeconds() {
    return refreshTokenValiditySeconds;
  }

}
