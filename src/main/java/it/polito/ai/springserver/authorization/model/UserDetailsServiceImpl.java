package it.polito.ai.springserver.authorization.model;

import it.polito.ai.springserver.authorization.model.repository.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepositoryInterface userRepositoryInterface;

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    User user = userRepositoryInterface.findByUsername(s);
    if(user == null)
      throw new UsernameNotFoundException(s + " doesn't exist.");
    return new UserDetailsImpl(user);
  }
}
