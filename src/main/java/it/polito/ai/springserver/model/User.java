package it.polito.ai.springserver.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

  @Id
  @SequenceGenerator(name = "users_user_id_seq", sequenceName = "users_user_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
  @Column(name = "user_id", columnDefinition = "BIGSERIAL", updatable = false, nullable = false)
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String email;

  @Column(name = "status", nullable = false)
  private UserStatus userStatus;


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Column(name = "role_id", nullable = false)
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

  public void addRole(Role role) {
    var newRole = new UserRole(this);
    newRole.setRole(role);
    this.userRoles.add(newRole);
  }

  public void removeRole(Role role) {
    Optional<UserRole> removed = this.userRoles.stream().
        filter(r -> r.getRole().equals(role)).findFirst();
    if (removed.isPresent()) {
      UserRole toRemove = removed.get();
      this.userRoles.remove(toRemove);
    }
    if (this.userRoles.size() == 0) {
      this.userRoles.add(new UserRole(this));
    }
  }

  public Long getId() {
    return id;
  }
}
