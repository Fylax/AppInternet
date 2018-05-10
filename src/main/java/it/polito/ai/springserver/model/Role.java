package it.polito.ai.springserver.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ADMIN, USER, CUSTOMER;

  @Override
  public String getAuthority() {
    return this.name();
  }
}