package it.polito.ai.authorization_server.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class UserRole {

  public enum Role implements GrantedAuthority {
    ADMIN, USER, CUSTOMER;

    @Override
    public String getAuthority() {
      return this.name();
    }
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name="id")
  private User user;

  private Role role;

  public UserRole() {}

  public UserRole(User user) {
    this.user = user;
    this.role = Role.USER;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

}


