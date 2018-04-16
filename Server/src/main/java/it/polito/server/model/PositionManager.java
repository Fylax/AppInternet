package it.polito.server.model;

import java.util.List;

public class PositionManager {

  //TODO: fare una select sull'ultima posizione inserita nel db se presente per inizializzare cachedPosition
  private Position cachedPosition = null;
  private final Distance distance = new HaversineDistance();

  void checkPositionValidity(List<Position> positions) throws PositionException {
    Position lastPosition = cachedPosition; //in questo modo modifico solo la variabile locale,
    // se qualcosa va storto l'ultima posizione inserita rimane invariata
    for(Position p : positions) {
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
  void add(User user, List<Position> positions) throws PositionException {
    if (positions.isEmpty()) {
      return;
    }
    checkPositionValidity(positions);
    PositionsDAO positionsDAO = new PostgresPositionDAO();
    positionsDAO.addPositions(user, positions);
  }

  List<Position> get(User user, long start, long end) {
    if (start == 0 && end == Long.MAX_VALUE) {
      return (new PostgresPositionDAO()).getPositions(user);
    }

      if (start > end) {
          return (new PostgresPositionDAO()).getPositions(user, end);
          //   return this.positions.stream().
          //      filter(p -> p.getTimestamp() >= end && p.getTimestamp() <= start).
          //    collect(Collectors.toList());
    }
    //return this.positions.stream().
    //        filter(p -> p.getTimestamp() >= start && p.getTimestamp() <= end).
    //        collect(Collectors.toList());

      return (new PostgresPositionDAO()).getPositions(user, start, end);

  }

  /*List<Position> get(User user) {
    return this.get(user, 0L, 0L);
  }

  List<Position> get(User user, long since) {
    return this.get(user, since, Long.MAX_VALUE);
  }*/
}
