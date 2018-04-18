package it.polito.server.model.dao.postgres;

import it.polito.server.model.Position;
import it.polito.server.model.dao.PositionsDAO;
import it.polito.server.model.User;
import org.postgresql.geometric.PGpoint;

import javax.ws.rs.InternalServerErrorException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PostgresPositionDAO implements PositionsDAO {

  @Override
  public void addPositions(User user, List<Position> positions) {
    String query = "INSERT INTO positions (t_stamp, curr_location, user_id)" +
                   "VALUES(?, POINT(?, ?), ?)";
    try (Connection connection = PostgresConnection.getConnection()) {
      connection.setAutoCommit(false);
      try (PreparedStatement ps = connection.prepareStatement(query)) {
        for (Position p : positions) {
          ps.setLong(1, p.getTimestamp());
          ps.setDouble(2, p.getLatitude());
          ps.setDouble(3, p.getLongitude());
          ps.setInt(4, user.getUid());
          ps.executeUpdate();
        }
        connection.commit();
      } catch (Exception e) {
        connection.rollback();
        e.printStackTrace();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  @Override
  public Position fetchLast(User user) {
    String query = "SELECT t_stamp, curr_location, user_id FROM positions WHERE user_id = ? ORDER BY t_stamp DESC LIMIT 1";
    return this.fetch(user, query).get(0);
  }

  @Override
  public List<Position> fetchAll(User user) {
    String query = "SELECT t_stamp, curr_location, user_id FROM positions WHERE user_id = ?";
    return this.fetch(user, query);
  }

  @Override
  public List<Position> fetchSince(User user, long since) {
    String query = "SELECT t_stamp, curr_location, user_id FROM positions WHERE user_id = ? AND t_stamp > ?";
    return this.fetch(user, query, since);
  }

  @Override
  public List<Position> fetchUpTo(User user, long upTo) {
    String query = "SELECT t_stamp, curr_location, user_id FROM positions WHERE user_id = ? AND t_stamp < ?";
    return this.fetch(user, query, upTo);
  }

  @Override
  public List<Position> fetchInterval(User user, long start, long end) {
    String query = "SELECT t_stamp, curr_location, user_id FROM positions WHERE user_id = ? AND t_stamp > ? AND t_stamp < ?";
    return this.fetch(user, query, start, end);
  }

  private List<Position> fetch(User user, String query, long... dates) {
    List<Position> currPositions = new LinkedList<>();

    try (Connection connection = PostgresConnection.getConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setInt(1, user.getUid());

      for (int i = 0; i < dates.length; i++) {
        ps.setLong(i + 1, dates[i]);
      }

      try (ResultSet resultSet = ps.executeQuery()) {
        while (resultSet.next()) {
          PGpoint point = (PGpoint) resultSet.getObject("curr_location");
          long t_stamp = resultSet.getLong("t_stamp");
          currPositions.add(new Position(point.x, point.y, t_stamp));
        }
      }
    } catch (SQLException e) {
      throw new InternalServerErrorException();
    }
    return currPositions;
  }
}
