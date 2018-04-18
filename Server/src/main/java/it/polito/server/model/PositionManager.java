package it.polito.server.model;

import it.polito.server.model.dao.PositionsDAO;
import it.polito.server.model.dao.postgres.PostgresPositionDAO;

import java.util.List;

public class PositionManager {

  private Position cachedPosition = null;
  private final Distance distance = new HaversineDistance();
  private final User holder;

  PositionManager(User user) {
    this.holder = user;
    this.cachedPosition = (new PostgresPositionDAO()).fetchLast(this.holder);
  }

  void checkPositionValidity(List<Position> positions) throws PositionException {
    Position lastPosition = cachedPosition; //in questo modo modifico solo la variabile locale,
    // se qualcosa va storto l'ultima posizione inserita rimane invariata
    for (Position p : positions) {
      if (lastPosition == null) {
        lastPosition = p;
      } else {
        // First check if timestamps are valid (if not, no distance computation is performed)
        // then compute the distance and get the speed in m/s.
        boolean valid = p.getTimestamp() > lastPosition.getTimestamp() &&
                        ((distance.getDistance(lastPosition, p)) /
                         (p.getTimestamp() - lastPosition.getTimestamp())) < 100;
        if (!valid) {
          throw new PositionException();
        }
        lastPosition = p;
      }
    }
    cachedPosition = lastPosition; //in caso tutto ok aggiorno la cachedPosition
  }

  /**
   * Checks a list of positions and, if they validate, it adds them to the ones already defined for
   * this user.
   * By validation it is meant that two consecutive positions must have strictly increasing
   * timestamps and a speed lower than 100 m/s.
   *
   * @param positions List of given user positions.
   */
  void add(List<Position> positions) throws PositionException {
    if (positions.isEmpty()) {
      return;
    }
    checkPositionValidity(positions);
    PositionsDAO positionsDAO = new PostgresPositionDAO();
    positionsDAO.addPositions(this.holder, positions);
  }

  List<Position> get(String start, String end) {
    boolean hasStart = true;
    boolean hasEnd = true;
    long startL = 0;
    try {
      startL = Math.round(Double.valueOf(start));
    } catch (NumberFormatException | NullPointerException e) {
      hasStart = false;
    }

    long endL = 0;
    try {
      endL = Math.round(Double.valueOf(end));
    } catch (NumberFormatException e) {
      hasEnd = false;
    }

    if(hasStart && hasEnd) {
      return (new PostgresPositionDAO()).fetchInterval(this.holder, startL, endL);
    } else if(!hasStart && !hasEnd) {
      return (new PostgresPositionDAO()).fetchAll(this.holder);
    } else if (hasStart) {
      return (new PostgresPositionDAO()).fetchSince(this.holder, startL);
    } else {
      return (new PostgresPositionDAO()).fetchUpTo(this.holder, endL);
    }
  }
}
