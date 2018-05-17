package it.polito.ai.springserver.resource.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
@CompoundIndex(def = "{'userid':1, 'timestamp':1}", name = "compound_index_1")
public class Position {

  @Id
  private String id;
  private long userid;
  private long timestamp;

  @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
  GeoJsonPoint point;

  public Position() {  } //used internally by MongoDb

  public Position(long userid, long timestamp, double longitude, double latitude) {
    this.userid = userid;
    this.timestamp = timestamp;
    this.point = new GeoJsonPoint(longitude, latitude);
  }

  public String getId() {
    return id;
  }

  public long getUserid() {
    return userid;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public GeoJsonPoint getPoint() {
    return point;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Position position = (Position) o;
    return userid == position.userid &&
            timestamp == position.timestamp &&
            Objects.equals(id, position.id) &&
            Objects.equals(point, position.point);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, userid, timestamp, point);
  }
}
