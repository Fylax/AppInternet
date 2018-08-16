package it.polito.ai.springserver.authorization.model.repository;

import it.polito.ai.springserver.authorization.model.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2ClientRepositoryInterface extends JpaRepository<OAuth2Client, String> {
  OAuth2Client findByClientId(String clientId);
}
