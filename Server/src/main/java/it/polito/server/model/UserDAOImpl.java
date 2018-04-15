package it.polito.server.model;

import java.sql.*;
import java.util.List;

import static it.polito.server.model.DbConnection.*;

public class UserDAOImpl implements it.polito.server.model.UserDAO {
  Connection connection = null;
  PreparedStatement ps = null;
  ResultSet resultSet = null;

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
    try {
      String queryString = "SELECT * FROM users";
      connection = getInstance().getConnection();
      ps = connection.prepareStatement(queryString);
      resultSet = ps.executeQuery();
      while (resultSet.next()) {
        System.out.println("username " + resultSet.getString("username")
                + ", secretHash " + resultSet.getString("secret")
                + ", email "     + resultSet.getString("email")
                + ", status " + resultSet.getString("status"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (resultSet != null) {
          resultSet.close();
        }
        if (ps != null){
          ps.close();
      }if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override
  public User findByEmail(String email) {
    return null;
  }

  @Override
  public boolean checkUser(int id) {
    return false;
  }
}
