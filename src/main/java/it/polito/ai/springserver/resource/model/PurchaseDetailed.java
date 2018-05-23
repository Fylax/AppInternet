package it.polito.ai.springserver.resource.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="purchase")
public class PurchaseDetailed extends Purchase {

  @Id
  private ObjectId id;
  private List<PurchasedPosition> positions;

  public PurchaseDetailed(long customerid, long timestamp, long start, long end, List<Position> positions) {
    super(customerid, timestamp, start, end, positions.size() * 1d); // TODO conversione in IOTA
    this.positions = new ArrayList<>(positions.size());
    positions.forEach(p -> this.positions.add(new PurchasedPosition(p)));
  }

  @PersistenceConstructor
  public PurchaseDetailed(ObjectId id, long customerid, long timestamp, long start, long end, double amount, List<PurchasedPosition> positions) {
    super(customerid, timestamp, start, end, positions.size() * 1d);
    this.positions = positions;
  }

  public List<Position> getPositions() {
    var pos = new ArrayList<Position>(this.positions.size());
    this.positions.forEach(p -> pos.add(p.getPosition()));
    return pos;
  }

  public String getId() {
    return String.valueOf(id);
  }

  @JsonIgnore
  public PurchaseSummary getSummary() {
    return new PurchaseSummary(super.customerid, super.timestamp, super.start, super.end, this.positions.size(), super.status);
  }
}
