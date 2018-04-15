package it.polito.server.model;

import javafx.geometry.Point2D;
import org.mindrot.jbcrypt.BCrypt;
import org.postgresql.geometric.PGpoint;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PostgresPositionDAO implements PositionsDAO{
    @Override
    public List<Position> getPositions(String user_id) {

        List<Position> currPositions = new LinkedList<>();
        String queryString = "SELECT pos_id, t_stamp, curr_location, user_id FROM positions WHERE user_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(queryString)) {
            ps.setString(1, user_id);

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    PGpoint point = (PGpoint) resultSet.getObject("curr_location");
                    Timestamp t_stamp = resultSet.getTimestamp("t_stamp");
                    currPositions.add(new Position(point.x, point.y , t_stamp.getTime()/1000));
                }
            }
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return currPositions;
    }

    @Override
    public List<Position> getPositions(String user_id, long since) {
        return null;
    }

    @Override
    public List<Position> getPositions(String user_id, long start, long end) {
        return null;
    }
}
