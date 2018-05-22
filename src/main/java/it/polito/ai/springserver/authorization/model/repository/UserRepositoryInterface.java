package it.polito.ai.springserver.authorization.model.repository;

import it.polito.ai.springserver.authorization.model.Role;
import it.polito.ai.springserver.authorization.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepositoryInterface extends JpaRepository<User, Long> {
  User findByUsername(String username);
  List<User> findAllByUserRolesContaining(Role role, Pageable pageable);
}
