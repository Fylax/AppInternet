package it.polito.ai.authorization_server.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

  @Id
  @SequenceGenerator(name="users_user_id_seq", sequenceName="users_user_id_seq", allocationSize=1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="usr_id_seq")
  @Column(name = "user_id", updatable = false, nullable = false)
  private Long id;

  private String username;

  private String password;

  private String email;

  @Column(name = "status")
  private UserStatus userStatus;

  @Column(name = "role")
  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST})
  private Set<UserRole> userRoles;

  public User() {

  }

  public User(String username, String password, String email, UserStatus userStatus) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.userStatus = userStatus;
    this.userRoles = new HashSet<>();
    this.userRoles.add(new UserRole(this));
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserStatus getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(UserStatus userStatus) {
    this.userStatus = userStatus;
  }

  public Set<Role> getRoles() {
    Set<Role> roles = new HashSet<>();
    this.userRoles.forEach(r -> roles.add(r.getRole()));
    return roles;
  }

}