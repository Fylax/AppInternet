package it.polito.server.model;

import java.util.List;

public interface UserDAO {
  void insert(User u);
  void delete(User u);
  void update(User u);
  User findById(int id);
  List<User> findAll();
  User findByEmail(String email);

}
