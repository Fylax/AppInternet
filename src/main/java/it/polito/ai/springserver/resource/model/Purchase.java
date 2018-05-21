package it.polito.ai.springserver.resource.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Purchase {

  @Id
  private String id;
  private long customerid;
  private long timestamp;
  private long start;
  private long end;
  private String status;
  private List<PurchasedPosition> positions;
  private double amount;
  @Transient
  private int countPosition;

  Purchase(){}

  @PersistenceConstructor
  public Purchase(long customerid, long timestamp, long start, long end, List<Position> positions) {
    this.customerid = customerid;
    this.timestamp = timestamp;
    this.start = start;
    this.end = end;
    this.status = "pending";
    this.positions = new ArrayList<>(positions.size());
    this.amount = this.positions.size()*1;              //fittiziamente costo unitario!
    this.countPosition = this.positions.size();
    positions.forEach(p -> this.positions.add(new PurchasedPosition(p)));
  }

  public long getCustomerid() {
    return customerid;
  }

  public long getTimestamp() {
    return timestamp;
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
