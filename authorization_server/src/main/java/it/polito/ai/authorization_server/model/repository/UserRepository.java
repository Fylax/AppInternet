package it.polito.ai.authorization_server.model.repository;

import it.polito.ai.authorization_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
}
