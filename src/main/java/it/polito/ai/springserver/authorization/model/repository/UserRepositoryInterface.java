package it.polito.ai.springserver.authorization.model.repository;

import it.polito.ai.springserver.authorization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryInterface extends JpaRepository<User, Long> {
  User findByUsername(String username);
}
