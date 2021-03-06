package it.polito.ai.springserver.resource.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.polito.ai.springserver.resource.controller.deserializer.CustomerRequestDeserializer;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.util.AbstractMap;

@JsonDeserialize(using = CustomerRequestDeserializer.class)
public class UserRequest {
  private AbstractMap.SimpleImmutableEntry<Long,Long> temporal_range;
  private GeoJsonPolygon area;


  public UserRequest(long start, long end, GeoJsonPolygon area) {
    this.temporal_range = new AbstractMap.SimpleImmutableEntry<>(start, end);
    this.area = area;

  }

  public long getStart() {
    return this.temporal_range.getKey();
  }

  public long getEnd() {
    return this.temporal_range.getValue();
  }

  public GeoJsonPolygon getPolygon() {
    return area;
  }

}
