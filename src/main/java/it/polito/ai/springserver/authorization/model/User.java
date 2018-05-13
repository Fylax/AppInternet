package it.polito.ai.springserver.authorization.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users",
        indexes = @Index(name = "username_index", columnList = "username", unique = true))
public class User {

  @Id
  @SequenceGenerator(name = "users_user_id_seq", sequenceName = "users_user_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
  @Column(name = "user_id", columnDefinition = "BIGSERIAL", updatable = false, nullable = false)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String email;

  @Column(name = "status", nullable = false)
  private UserStatus userStatus;

  //for default the fetch type is lazy, but this cause problem when retrieve the roles
  //to create the JWT. I think we can use the EAGER fetch type because at most the user has three roles
  //so performance are almost the same
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
          name="roles",
          joinColumns=@JoinColumn(name="user_id")
  )
  @Column(name = "user_role", nullable = false)
  private List<Role> userRoles;

  public User() {

  }

  public User(String username, String password, String email, UserStatus userStatus) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.userStatus = userStatus;
    this.userRoles = new ArrayList<>();
    this.userRoles.add(Role.USER);
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

  public List<Role> getRoles() {
    return this.userRoles;
  }

  public void addRole(Role role) {
    this.userRoles.add(role);
  }

  public void removeRole(Role role) {
    this.userRoles.remove(role);
  }

  public Long getId() {
    return id;
  }
}
