package it.polito.ai.springserver.resource.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.AbstractMap;

@Document
public class Purchase {

  @Id
  private String id;
  private long customerid;
  private long timestamp;
  private GeoJsonPolygon area;
  private AbstractMap.SimpleImmutableEntry<Long,Long> temporal_range;

  public Purchase(long customerid, long timestamp, GeoJsonPolygon polygon, long start, long end) {
    this.customerid = customerid;
    this.timestamp = timestamp;
    this.area = polygon;
    this.temporal_range = new AbstractMap.SimpleImmutableEntry(start, end);
  }

}
