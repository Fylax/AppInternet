package it.polito.ai.authorization_server.model.repository;

import it.polito.ai.authorization_server.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
