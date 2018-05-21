package it.polito.ai.springserver.resource.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Purchase {

  @Id
  private ObjectId id;
  private long customerid;
  private long timestamp;
  private long start;
  private long end;
  private TransactionStatus status;
  private List<PurchasedPosition> positions;
  private double amount;
  @Transient
  private int countPosition;

  Purchase(){}

  public Purchase(long customerid, long timestamp, long start, long end, List<Position> positions) {
    this.customerid = customerid;
    this.timestamp = timestamp;
    this.start = start;
    this.end = end;
    this.status = TransactionStatus.PENDING;
    this.positions = new ArrayList<>(positions.size());
    positions.forEach(p -> this.positions.add(new PurchasedPosition(p)));
    this.amount = this.positions.size()*1d;              //fittiziamente costo unitario!
    this.countPosition = this.positions.size();
  }

  @PersistenceConstructor
  public Purchase(ObjectId id, long customerid, long timestamp, long start, long end, double amount, List<PurchasedPosition> positions) {
    this.id = id;
    this.customerid = customerid;
    this.timestamp = timestamp;
    this.start = start;
    this.end = end;
    this.status = TransactionStatus.PENDING;
    this.positions = positions;
    this.amount = amount;
    this.countPosition = this.positions.size();
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

  public void clearPurchasedPositions(){
    this.positions.clear();
  }

  public long getStart() {
    return start;
  }

  public long getEnd() {
    return end;
  }

  public TransactionStatus getStatus() {
    return status;
  }

  public double getAmount() {
    return amount;
  }

  public int getCountPosition() {
    return countPosition;
  }

  public void setStatus(TransactionStatus status) {
    this.status = status;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}
