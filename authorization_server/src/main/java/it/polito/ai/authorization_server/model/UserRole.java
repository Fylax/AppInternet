package it.polito.ai.authorization_server.model;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class UserRole {

  @Id
  @SequenceGenerator(name="roles_role_id_seq", sequenceName="roles_role_id_seq", allocationSize=1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="roles_role_id_seq")
  @Column(name="role_id", columnDefinition="BIGSERIAL", updatable = false, nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name="user_id")
  @Column(nullable = false)
  private User user;

  @Column(nullable = false)
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


