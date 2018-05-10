package it.polito.ai.springserver.model.repository;

import it.polito.ai.springserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
}
