package it.polito.ai.springserver.resource.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

@Document
public class Purchase {

  @Id
  private String id;
  private long customerid;
  private long timestamp;
  private AbstractMap.SimpleImmutableEntry<Long,Long> temporal_range;
  private List<PurchasedPosition> positions;

  public Purchase(long customerid, long timestamp, long start, long end, List<Position> positions) {
    this.customerid = customerid;
    this.timestamp = timestamp;
    this.temporal_range = new AbstractMap.SimpleImmutableEntry<>(start, end);
    this.positions = new ArrayList<>(positions.size());
    positions.forEach(p -> this.positions.add(new PurchasedPosition(p)));
  }

  public long getCustomerid() {
    return customerid;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public long getStart() {
    return this.temporal_range.getKey();
  }

  public long getEnd() {
    return this.temporal_range.getValue();
  }

  public List<Position> getPositions() {
    var pos = new ArrayList<Position>(this.positions.size());
    this.positions.forEach(p -> pos.add(p.getPosition()));
    return pos;
  }

  List<String> getPositionIds() {
    var ids = new ArrayList<String>(this.positions.size());
    this.positions.forEach(p -> ids.add(p.getPosition().getId()));
    return ids;
  }
}
