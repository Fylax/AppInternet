package it.polito.ai.springserver.authorization.model;

import javax.persistence.*;

@Entity
@Table(name = "oauth_client_details",
    indexes = @Index(name = "client_index", columnList = "client_id", unique = true))
public class OAuth2Client {

  @Id
  @Column(name = "client_id", nullable = false, unique = true)
  private String clientId;

  @Column(name = "client_secret", nullable = false)
  private String clientSecret;

  public OAuth2Client() {}

  public OAuth2Client(String clientId, String clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  public String getClientId() {
    return clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

}