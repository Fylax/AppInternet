package it.polito.ai.authorization_server.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ADMIN, USER, CUSTOMER;

  @Override
  public String getAuthority() {
    return this.name();
  }
}