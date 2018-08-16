package it.polito.ai.springserver.authorization.model;

import it.polito.ai.springserver.authorization.model.repository.OAuth2ClientRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service("ClientDetailsService")
public class ClientDetailsServiceImpl implements ClientDetailsService {

  @Autowired
  private OAuth2ClientRepositoryInterface clientRepository;

  @Autowired
  private ClientConfiguration configuration;

  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    var client = clientRepository.findByClientId(clientId);
    if (client == null) {
      throw new AuthenticationCredentialsNotFoundException(clientId + " does not exist.");
    }
    return new ClientDetailsImpl(client, configuration);
  }
}
