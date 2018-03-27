package it.polito.ese1.model;

import java.util.LinkedList;
import java.util.List;

public class GlobalPositions {

  private static final double R = 6371e3;
  private final List<GlobalPosition> positions = new LinkedList<>();

  private double getHaversineDistance(GlobalPosition src, GlobalPosition dst) {
    double lat1 = Math.toRadians(src.getLatitude());
    double lat2 = Math.toRadians(dst.getLatitude());
    double deltaLng = Math.toRadians(dst.getLongitude() - src.getLongitude());

    double latHaversine = (1 - Math.cos(lat2 - lat1)) / 2;
    double lngHaversine = (1 - Math.cos(deltaLng)) / 2;

    double sqrtArg = latHaversine + (Math.cos(lat1) * Math.cos(lat2) * lngHaversine);

    return 2 * R * Math.asin(Math.sqrt(sqrtArg));
  }

  /**
   * Checks a list of positions and, if they validate, it adds them to the ones already defined for
   * this user.
   * By validation it is meant that two consecutive positions must have strictly increasing
   * timestamps and a speed lower than 100 m/s.
   *
   * @param positions List of given user positions.
   */
  public void addPositions(List<GlobalPosition> positions) throws PositionException {
    if (positions.isEmpty()) {
      return;
    }

    int i;
    GlobalPosition reference;
    if (this.positions.isEmpty()) {
      i = 1;
      reference = positions.get(0);
    } else {
      i = 0;
      reference = this.positions.get(this.positions.size());
    }

    int numPositions = positions.size();
    for (; i < numPositions; i++) {
      GlobalPosition current = positions.get(i);
      // First check if timestamps are valid (if not, no distance computation is performed)
      // then compute the distance and get the speed in mm/ms (which is equal to m/s).
      boolean valid = current.getTimestamp().after(reference.getTimestamp()) &&
                      ((this.getHaversineDistance(reference, current) * 1000) /
                       (current.getTimestamp().getTime() - reference.getTimestamp().getTime())) < 100;
      if (!valid) {
        throw new PositionException();
      }
      reference = current;
    }
    this.positions.addAll(positions);
  }

  public List<GlobalPosition> getPositions() {
    return this.positions;
  }
}
