package it.polito.ai.springserver.resource.model;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Objects;

public class Point implements Comparable {

  private double lon;
  private double lat;

  public Point(GeoJsonPoint point) {
    this.lon = point.getX();
    this.lat = point.getY();
  }

  public Point() {
  }

  public Point(double lon, double lat) {
    this.lon = lon;
    this.lat = lat;
  }

  @Override
  public int compareTo(Object o) {
    Point p = (Point) o;
    if (this.equals(p)) {
      return 0;
    }
    if (this.getLon() > p.getLon()) {
      return 1;
    } else if (this.getLon() == p.getLon()) {
      if (this.getLat() > p.getLat()) {
        return 1;
      }
    }
    return -1;
  }

  public double getLon() {
    return lon;
  }

  public double getLat() {
    return lat;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Point)) return false;
    Point point = (Point) o;
    return Double.compare(point.getLon(), getLon()) == 0 &&
            Double.compare(point.getLat(), getLat()) == 0;
  }

  @Override
  public int hashCode() {

    return Objects.hash(getLon(), getLat());
  }
}
