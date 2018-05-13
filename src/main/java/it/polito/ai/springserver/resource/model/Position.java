package it.polito.ai.springserver.resource.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndex(def = "{'userid':1, 'timestamp':1}", name = "compound_index_1")
public class Position {

  @Id
  private String id;
  private long userid;
  private long timestamp;

  @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
  GeoJsonPoint position;

  public Position(long userid, long timestamp, double longitude, double latitude) {
    this.userid = userid;
    this.timestamp = timestamp;
    this.position = new GeoJsonPoint(longitude, latitude);
  }

  public long getUserid() {
    return userid;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
