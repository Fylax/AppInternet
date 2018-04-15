package it.polito.server.model;

import java.util.List;

public interface UserDAO {
  void insert(User u);
  void delete(User u);
  void update(User u);
  User getUser(final String username, final String password) throws InvalidLoginException;
  List<User> findAll();
  User findByEmail(String email);

}
