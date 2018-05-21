package it.polito.ai.springserver.resource.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.polito.ai.springserver.resource.controller.deserializer.UserPositionDeserializer;
import it.polito.ai.springserver.resource.exception.PositionException;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
@CompoundIndex(def = "{'userid':1, 'timestamp':1}", name = "compound_index_1")
@JsonDeserialize(using = UserPositionDeserializer.class)
public class Position {

  @Id
  private ObjectId id;
  private long userid;
  private long timestamp;

  @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
  private GeoJsonPoint point;

  public Position() {
  } //used internally by MongoDb

  public Position(long userid, long timestamp, double longitude, double latitude) {
    this.userid = userid;
    this.timestamp = timestamp;
    this.point = new GeoJsonPoint(longitude, latitude);
  }

  public Position(long timestamp, double longitude, double latitude) throws PositionException {
    this.userid = -1;
    this.timestamp = timestamp;
    boolean valid = (latitude >= -90) &&
                    (latitude <= 90) &&
                    (longitude >= -180) &&
                    (longitude <= 180);
    if (!valid) {
      throw new PositionException("Invalid coordinates.");
    }
    this.point = new GeoJsonPoint(longitude, latitude);
  }

  @JsonIgnore
  public ObjectId getId() {
    return id;
  }

  public long getUserid() {
    return userid;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public double getLongitude() {
    return this.point.getCoordinates().get(0);
  }

  public double getLatitude() {
    return this.point.getCoordinates().get(1);
  }

  public void setUserid(long userid) {
    this.userid = userid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
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
