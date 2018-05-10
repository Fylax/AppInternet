package it.polito.ai.springserver.model.repository;

import it.polito.ai.springserver.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
