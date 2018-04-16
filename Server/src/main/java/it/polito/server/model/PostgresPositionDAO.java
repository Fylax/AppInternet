package it.polito.server.model;

import org.postgresql.geometric.PGpoint;
import javax.ws.rs.InternalServerErrorException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PostgresPositionDAO implements PositionsDAO{

    @Override
    public void addPositions(User user, List<Position> positions){
      PreparedStatement ps = null;
      Connection connection = null;
      String query = "INSERT INTO positions (t_stamp, curr_location, user_id)" +
              "VALUES(?, POINT(?, ?), ?)";
      try {
        connection = DbConnection.getConnection();
        try {
          connection.setAutoCommit(false);
          ps = connection.prepareStatement(query);
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
      }
      catch (SQLException e){
        e.printStackTrace();
      }finally {
        if(ps != null){
          try {
            ps.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
        if(connection != null){
          try {
            connection.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }

    @Override
    public List<Position> getPositions(User user) {

      List<Position> currPositions = new LinkedList<>();
      String queryString = "SELECT t_stamp, curr_location, user_id FROM positions WHERE user_id = ?";

      try (Connection connection = DbConnection.getConnection();
           PreparedStatement ps = connection.prepareStatement(queryString)) {
        ps.setInt(1, user.getUid());

        try (ResultSet resultSet = ps.executeQuery()) {
          while (resultSet.next()) {
            PGpoint point = (PGpoint) resultSet.getObject("curr_location");
            long t_stamp = resultSet.getLong("t_stamp");
            currPositions.add(new Position(point.x, point.y , t_stamp));
          }
        }
      } catch (SQLException e) {
        throw new InternalServerErrorException();
      }
      return currPositions;
    }

    @Override
    public List<Position> getPositions(User user, long since) {
        return null;
    }

    @Override
    public List<Position> getPositions(User user, long start, long end) {
        return null;
    }
}
