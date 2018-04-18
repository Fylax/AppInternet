package it.polito.server.model.dao.postgres;

import it.polito.server.model.InvalidLoginException;
import it.polito.server.model.User;
import it.polito.server.model.dao.UserDAO;
import it.polito.server.model.UserStatus;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.InternalServerErrorException;
import java.sql.*;
import java.util.List;

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
  public User getUser(final String username, final String password) throws InvalidLoginException {
    String queryString = "SELECT uid, secret, email, status FROM users WHERE username = ?";
    try (Connection connection = PostgresConnection.getConnection();
         PreparedStatement ps = connection.prepareStatement(queryString)) {
          ps.setString(1, username);
      try (ResultSet resultSet = ps.executeQuery()) {
        if (!resultSet.next()) {
          throw new InvalidLoginException(); // User not found
        }
        String secret = resultSet.getString("secret");
        if (!BCrypt.checkpw(password, secret)) {
          throw new InvalidLoginException();
        }
        int uid = resultSet.getInt("uid");
        String email = resultSet.getString("email");
        UserStatus status = UserStatus.valueOf(resultSet.getString("status"));
        // TODO exception on status != approved
        return new User(uid, username, email, status);
      }
    } catch (SQLException e) {
      throw new InternalServerErrorException();
    }
  }

  @Override
  public List<User> findAll() {
    String queryString = "SELECT * FROM users";
    try (
        Connection connection = PostgresConnection.getConnection();
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
    throw new UnsupportedOperationException();
  }


}
