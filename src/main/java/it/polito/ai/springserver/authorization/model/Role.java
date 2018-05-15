package it.polito.ai.springserver.authorization.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ROLE_ADMIN, ROLE_USER, ROLE_CUSTOMER;

  @Override
  public String getAuthority() {
    return this.name();
  }
}