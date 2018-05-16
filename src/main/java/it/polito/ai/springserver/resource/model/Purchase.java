package it.polito.ai.springserver.resource.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.AbstractMap;

@Document
public class Purchase extends CustomerRequest {

  @Id
  private String id;
  private long customerid;
  private long timestamp;

  public Purchase(long customerid, long timestamp, GeoJsonPolygon polygon, long start, long end) {
    super(start, end, polygon);
    this.customerid = customerid;
    this.timestamp = timestamp;
  }

}
