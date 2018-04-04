package it.polito.ese1.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Positions {

  private final List<Position> positions = new LinkedList<>();

  /**
   * Checks a list of positions and, if they validate, it adds them to the ones already defined for
   * this user.
   * By validation it is meant that two consecutive positions must have strictly increasing
   * timestamps and a speed lower than 100 m/s.
   *
   * @param positions List of given user positions.
   */
  void addPositions(List<Position> positions) throws PositionException {
    if (positions.isEmpty()) {
      return;
    }

    int i;
    Position reference;
    if (this.positions.isEmpty()) {
      i = 1;
      reference = positions.get(0);
    } else {
      i = 0;
      reference = this.positions.get(this.positions.size() - 1);
    }

    Distance distance = new HaversineDistance();
    int numPositions = positions.size();
    for (; i < numPositions; i++) {
      Position current = positions.get(i);
      // First check if timestamps are valid (if not, no distance computation is performed)
      // then compute the distance and get the speed in m/s.
      boolean valid = current.getTimestamp() > reference.getTimestamp() &&
                      ((distance.getDistance(reference, current)) /
                       (current.getTimestamp() - reference.getTimestamp())) < 100;
      if (!valid) {
        throw new PositionException();
      }
      reference = current;
    }
    this.positions.addAll(positions);
  }


  List<Position> getPositions() {
    return this.getPositions(0L, 0L);
  }

  List<Position> getPositions(long since) {
    return this.getPositions(since, Long.MAX_VALUE);
  }

  List<Position> getPositions(long start, long end){
    if (start == 0 && end == 0) {
      return new ArrayList<>(this.positions);
    }
    if (start > end) {
      return this.positions.stream().
              filter(p -> p.getTimestamp() >= end && p.getTimestamp() <= start).
              collect(Collectors.toList());
    }
    return this.positions.stream().
        filter(p -> p.getTimestamp() >= start && p.getTimestamp() <= end).
        collect(Collectors.toList());
  }
}
