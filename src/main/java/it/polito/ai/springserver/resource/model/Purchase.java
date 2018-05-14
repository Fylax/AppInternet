package it.polito.ai.springserver.resource.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Purchase {

  @Id
  private String id;
  private long userid;
  private long timestamp;
  private GeoJsonPolygon area;

  // TODO decidere granularità aquisto: sempre? giorno specifico? fino a timestamp? range temporale?

  public Purchase(long userid, long timestamp, GeoJsonPolygon polygon) {
    this.userid = userid;
    this.timestamp = timestamp;
    this.area = polygon;
  }

}
