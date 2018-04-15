package it.polito.server.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Positions {

  // TODO deve diventare cachedPosition
  private final LinkedList<Position> positions = new LinkedList<>();
  private final Distance distance = new HaversineDistance();

  void add(Position position) throws PositionException {
    if (this.positions.isEmpty()) {
      this.positions.add(position);
    } else {
      Position reference = this.positions.getLast();
      // First check if timestamps are valid (if not, no distance computation is performed)
      // then compute the distance and get the speed in m/s.
      boolean valid = position.getTimestamp() > reference.getTimestamp() &&
                      ((distance.getDistance(reference, position)) /
                       (position.getTimestamp() - reference.getTimestamp())) < 100;
      if (!valid) {
        throw new PositionException();
      }
    }
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

    for (var position: positions) {
      this.add(position);
    }
  }

  List<Position> get(User user, long start, long end) {
    if (start == 0 && end == 0) {
      return new ArrayList<>(this.positions);
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

      return (new PostgresPositionDAO()).getPositions(user);

  }

  List<Position> get(User user) {
    return this.get(user, 0L, 0L);
  }

  List<Position> get(User user, long since) {
    return this.get(user, since, Long.MAX_VALUE);
  }
}
