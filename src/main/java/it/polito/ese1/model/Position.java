package it.polito.ese1.model;

public class Position {

  private final double latitude;
  private final double longitude;
  private final long timestamp;

  public Position(double latitude, double longitude, long timestamp) {
    boolean valid = (latitude >= -90) &&
                    (latitude <= 90) &&
                    (longitude >= -180) &&
                    (longitude <= 180);
    if (!valid) {
      throw new IllegalArgumentException();
    }
    this.latitude = latitude;
    this.longitude = longitude;
    this.timestamp = timestamp;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public double getLongitude() {
    return this.longitude;
  }

  public long getTimestamp() {
    return this.timestamp;
  }

}
