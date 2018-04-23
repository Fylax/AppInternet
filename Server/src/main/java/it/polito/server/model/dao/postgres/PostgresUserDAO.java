package it.polito.server.model.dao.postgres;

import it.polito.server.model.*;
import it.polito.server.model.dao.UserDAO;
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
  public User getUser(final String username, final String password) throws InvalidLoginException, ConnectionException, UserStatusException {
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
        if(status != UserStatus.APPROVED){
          throw new UserStatusException();
        }
        return new User(uid, username, email, status);
      }
    } catch (SQLException e) {
      throw new ConnectionException(e);
    }
  }

  @Override
  public User findByEmail(String email) {
    throw new UnsupportedOperationException();
  }


}
