package it.polito.server.model;

import java.sql.*;
import java.util.List;

import static it.polito.server.model.DbConnection.*;

public class PostgresUserDAO implements UserDAO {

  @Override
  public void insert(User u) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(User u) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void update(User u) {
    throw new UnsupportedOperationException();
  }

  @Override
  public User findById(int id) {

    return null;
  }

  @Override
  public List<User> findAll() {
    String queryString = "SELECT * FROM users";
    try (
        Connection connection = DbConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(queryString);
        ResultSet resultSet = ps.executeQuery()) {
      while (resultSet.next()) {
        System.out.println("username " + resultSet.getString("username")
                           + ", secretHash " + resultSet.getString("secret")
                           + ", email " + resultSet.getString("email")
                           + ", status " + resultSet.getString("status"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public User findByEmail(String email) {
    return null;
  }

  public boolean checkUser(int id) {
    return false;
  }
}
