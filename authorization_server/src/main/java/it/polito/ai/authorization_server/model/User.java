package it.polito.ai.authorization_server.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "user_id")
  private Long id;

  private String username;

  private String password;

  private String email;

  @Column(name = "user_status")
  private  UserStatus userStatus;

  public User() {

  }

  public User(String username, String password, String email, UserStatus userStatus) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.userStatus = userStatus;
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

}
