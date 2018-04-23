package it.polito.server.model.dao;

import it.polito.server.model.ConnectionException;
import it.polito.server.model.InvalidLoginException;
import it.polito.server.model.User;
import it.polito.server.model.UserStatusException;

import java.util.List;

public interface UserDAO {
  void insert(User u);
  void delete(User u);
  void update(User u);
  User getUser(final String username, final String password) throws InvalidLoginException, ConnectionException, UserStatusException;
  User findByEmail(String email);

}
